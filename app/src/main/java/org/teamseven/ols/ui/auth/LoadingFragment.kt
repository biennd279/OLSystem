package org.teamseven.ols.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentLoadingBinding
import org.teamseven.ols.utils.SessionManager
import timber.log.Timber


class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLoadingBinding.inflate(inflater)
        sessionManager = SessionManager(requireContext())
        navController = findNavController()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        navController.navigate(
            //LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment()
            LoadingFragmentDirections.actionLoadingFragmentToHomeFragment()
        )
        */

        // check user signing state through SessionManager
        if (sessionManager.token.isNullOrEmpty()) {
            // user didn't sign in yet
            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment()
            )
        } else {
            // user signed in, loading data
            // get user information


            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToHomeFragment()
            )
        }


    }
}