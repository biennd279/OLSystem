package org.teamseven.ols.ui.classes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.MainActivity
import org.teamseven.ols.databinding.FragmentAddMemberBinding
import org.teamseven.ols.databinding.FragmentHomeBinding

class AddMemberFragment : Fragment() {

    private lateinit var binding : FragmentAddMemberBinding
    private lateinit var navController : NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater)
        navController = findNavController()

        

        return binding.root
    }

}