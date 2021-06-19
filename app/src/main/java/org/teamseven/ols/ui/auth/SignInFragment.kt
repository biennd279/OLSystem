package org.teamseven.ols.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentSignInBinding
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.SignInViewModel
import timber.log.Timber

class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater)

        navController = findNavController()

        sessionManager = SessionManager(requireContext())

        viewModel = SignInViewModel(requireContext())

        binding.buttonSignin.setOnClickListener {
            launchSignInFlow()
        }

        return binding.root
    }

    private fun launchSignInFlow() {
        val loginRequest = LoginRequest(
            binding.email.text.toString(),
            binding.password.text.toString()
        )

        lifecycleScope.launch {
            viewModel.signIn(loginRequest).collect {
                when (it.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.SUCCESS -> {
                        Timber.i("Log in success with ${it.data}")
                        it.data?.let { data ->
                            sessionManager.token = data.token
                            sessionManager.userId = data.user.id
                        }

                        // the right direction is to the Loading to prepare the data
                        //navController.navigate(SignInFragmentDirections.actionSignInFragmentToLoadingFragment())

                        navController.navigate(
                            R.id.homeFragment
                        )
                    }
                    Resource.Status.ERROR -> {
                        // make a Toast or something to the Activity Context, to show error message
                    }
                }
            }
        }
    }

}