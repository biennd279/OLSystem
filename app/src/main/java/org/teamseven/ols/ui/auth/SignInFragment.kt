package org.teamseven.ols.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentSignInBinding
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.db.UserDao
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.repositories.UserRepository
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.SignInViewModel
import timber.log.Timber

@InternalCoroutinesApi
class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var binding: FragmentSignInBinding

    private lateinit var navController: NavController
    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var userDao: UserDao
    private lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_in, container, false)

        navController = findNavController()

        authService = AuthService.create(requireContext())
        userService = UserService.create(requireContext())!!
        appDatabase = Room.inMemoryDatabaseBuilder(requireContext(), AppDatabase::class.java).build()
        userDao = appDatabase.userDao()

        userRepository = UserRepository(
            userService,
            authService,
            userDao
        )

        sessionManager = SessionManager(requireContext())

        binding.buttonLogin.setOnClickListener{
            launchSignInFlow()
        }

        binding.forgotPasswordTextview.setOnClickListener {
            navController.navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
        }

        return binding.root
    }

    private fun launchSignInFlow() = runBlocking {
        val loginRequest = LoginRequest(binding.username.text.toString(), binding.password.text.toString())
        Log.e("justACheck", loginRequest.toString())

        userRepository.login(loginRequest)
            .catch { e ->
                emit(Resource.error(null, e.message.toString()))
                Timber.d(e)
            }
            .collect {
                when (it.status) {
                    Resource.Status.LOADING -> TODO()
                    Resource.Status.SUCCESS -> {
                        it.data?.let { it1 -> {
                            sessionManager.saveAuthToken(it1.token)
                            sessionManager.saveUserId(it1.user.id)
                        } }

                        navController.navigate(R.id.homeFragment)
                    }
                    Resource.Status.ERROR -> TODO()
                }
            }
    }

}