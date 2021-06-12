package org.teamseven.ols.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentSignOptionBinding


class SignOptionFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignOptionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignOptionBinding.inflate(inflater)

        navController = findNavController()

        binding.signInOption.setOnClickListener {
            navController.navigate(SignOptionFragmentDirections.actionSignOptionFragmentToSignInFragment())
        }

        binding.signUpOption.setOnClickListener {
            navController.navigate(SignOptionFragmentDirections.actionSignOptionFragmentToSignUpFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.loadingFragment, true)
        }

    }
}