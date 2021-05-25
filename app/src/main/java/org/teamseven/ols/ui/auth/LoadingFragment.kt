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
import org.teamseven.ols.utils.SessionManager


class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoadingBinding.inflate(inflater)

        sessionManager = SessionManager(requireContext())

        navController = findNavController()

        if (sessionManager.fetchAuthToken().isNullOrEmpty()) {
            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment()
            )
        } else {
            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToHomeFragment()
            )
        }

        return binding.root
    }

}