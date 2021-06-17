package org.teamseven.ols.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentLoadingBinding
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.LoadingViewModel
import timber.log.Timber


class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var loadingViewModel: LoadingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoadingBinding.inflate(inflater)
        sessionManager = SessionManager(requireContext())
        navController = findNavController()
        loadingViewModel = LoadingViewModel(requireContext())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //remove all from backstack (navigate from "sign_out" - overflow_menu only)
        //or create a direction and use the popbackstack - inclusive -> read about it latter

        setUpController()
    }

    private fun setUpController() {
        //it is the test, remove it latter
//        navController.navigate(
//            //LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment()
//            LoadingFragmentDirections.actionLoadingFragmentToHomeFragment()
//        )
//


        // check user signing state through SessionManager
        if (sessionManager.token.isNullOrEmpty()) {
            // user didn't sign in yet (the first time or user sign out)
            // check the data in Room database. if it existed, delete it all
            // call function from LoadingViewModel
            // then navigate it into SignOptionFragment
            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToSignOptionFragment()
            )
        } else {
            // user have just signed in from SignInFragment / SignUpFragment
            // or signed before - the data is already in Room
            // check it and fetch it => call function from LoadingViewModel
            // then navigate it into HomeFragment
            navController.navigate(
                LoadingFragmentDirections.actionLoadingFragmentToHomeFragment()
            )
        }


    }

}