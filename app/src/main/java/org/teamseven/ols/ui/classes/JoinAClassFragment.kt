package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentJoinAClassBinding

class JoinAClassFragment : Fragment() {

    private lateinit var binding: FragmentJoinAClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJoinAClassBinding.inflate(inflater)


        return binding.root
    }

}