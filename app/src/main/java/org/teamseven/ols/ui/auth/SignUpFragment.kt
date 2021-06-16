package org.teamseven.ols.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentSignUpBinding
import org.teamseven.ols.entities.requests.LoginRequest
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.SignInViewModel
import org.teamseven.ols.viewmodel.SignUpViewModel
import timber.log.Timber


@InternalCoroutinesApi
class SignUpFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater)
        navController = findNavController()

        // create drop-down menu with spinner
        spinner = binding.roleSpinner
        setSpinner()

        // signup event
        binding.buttonSignup.setOnClickListener {
            launchSignUpFlow()
        }

        return binding.root
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.list_of_role, R.layout.simple_spinner_item
        ).also { adapter = it }
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner.adapter = HintSpinnerAdapter(adapter, R.layout.item_row_spinner, -1, requireContext())
        spinner.onItemSelectedListener = this
    }


    private fun launchSignUpFlow() {

        /*
        val signupRequest = SignupRequest(
            binding.name.text.toString(),
            binding.email.text.toString(),
            binding.roleSpinner.getSelectedItemPosition(),
            binding.password.text.toString()
        )

        lifecycleScope.launch {
            viewModel.signUp(signupRequest).collect {
                when (it.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.SUCCESS -> {
                        Timber.i("Sign up success with ${it.data}")
                        it.data?.let { data ->
                            sessionManager.token = data.token
                            sessionManager.userId = data.user.id
                        }

                        // the right direction is to the Loading to prepare the data
                        //navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToLoadingFragment())

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

         */


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(activity, "Please chose your role", Toast.LENGTH_SHORT).show()
    }


}