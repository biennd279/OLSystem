package org.teamseven.ols.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentLoadingBinding


class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_loading, container, false)

        navController = findNavController()

        navController.navigate(LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment())
        return binding.root
    }

}