package org.teamseven.ols.ui.classes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentHomeBinding
import org.teamseven.ols.ui.classes.all_classes.AllClassesFragment

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var navController : NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        navController = findNavController()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.loadingFragment, true)
        }

         */
    }

    override fun onStart() {
        super.onStart()

        Log.e("check_drawer_setup", "onStart called")
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.setUpCurrentClass()

    }
}