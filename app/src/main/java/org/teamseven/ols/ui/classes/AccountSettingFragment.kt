package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentAccountSettingBinding
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.UserViewModel
import org.teamseven.ols.viewmodel.UserViewModelFactory
import timber.log.Timber


class AccountSettingFragment() : Fragment() {

    private lateinit var binding: FragmentAccountSettingBinding

    private val userService by lazy { UserService.create(requireContext()) }

    private val authService by lazy { AuthService.create(requireContext()) }

    private val appDatabase by lazy { AppDatabase.create(requireContext()) }

    private val userViewModel: UserViewModel by activityViewModels() {
        UserViewModelFactory(
            userService = userService,
            authService = authService,
            appDatabase = appDatabase,
            application = requireActivity().application
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountSettingBinding.inflate(inflater)

        //change the password
        binding.textAccountSettingChangePassword.setOnClickListener {
            //no layout was created
        }

        binding.btnAccountSettingSave.setOnClickListener {
            //No Api ????
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccountSetting()
    }

    private fun getAccountSetting() {
        userViewModel.currentUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                    if (it.data == null) {
                        Timber.i(it.message)
                        return@observe
                    }

                    binding.edittextAccountSettingEmail.setText(it.data.email)
                    binding.edittextAccountSettingName.setText(it.data.name)

                }
                Resource.Status.ERROR -> Timber.i(it.message)
            }
        }
    }

}