package org.teamseven.ols.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentSignInBinding
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.SignInViewModel

@InternalCoroutinesApi
class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater)

        navController = findNavController()

        viewModel = SignInViewModel(requireContext())

        sessionManager = SessionManager(requireContext())

        binding.buttonLogin.setOnClickListener {
            launchSignInFlow()
        }

        binding.forgotPasswordTextview.setOnClickListener {
            navController.navigate(
                SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
            )
        }

        return binding.root
    }

    private fun launchSignInFlow() {
        val loginRequest = LoginRequest(
            binding.username.text.toString(),
            binding.password.text.toString()
        )

        lifecycleScope.launch {
            viewModel.signIn(loginRequest)
                .collect {
                    when (it.status) {
                        Resource.Status.LOADING -> {}
                        Resource.Status.SUCCESS -> {
                            it.data?.let { it1 ->
                                {
                                    sessionManager.saveAuthToken(it1.token)
                                    sessionManager.saveUserId(it1.user.id)
                                }
                            }

                            navController.navigate(R.id.homeFragment)
                        }
                        Resource.Status.ERROR -> {}
                    }
                }
        }
    }

}