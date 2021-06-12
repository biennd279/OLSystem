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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("check_drawer_init", "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        navController = findNavController()

        Log.e("check_drawer_init", "onCreateView")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.loadingFragment, true)
        }

         */

        Log.e("check_drawer_init", "onViewCreated")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.e("check_drawer_init", "onSaveInstanceState")

    }

    override fun onResume() {
        super.onResume()


        Log.e("check_drawer_init", "onResume")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("check_drawer_init", "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.e("check_drawer_init", "onStop")

    }

    override fun onPause() {
        super.onPause()
        Log.e("check_drawer_init", "onPause")
    }

    override fun onStart() {
        super.onStart()

        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.setUpDefault()

        Log.e("check_drawer_init", "onStart")

    }
}