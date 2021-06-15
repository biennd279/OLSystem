package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentAccountSettingBinding
import org.teamseven.ols.databinding.FragmentLoadingBinding


class AccountSettingFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountSettingBinding.inflate(inflater)
        return binding.root
    }

}