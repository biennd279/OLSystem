package org.teamseven.ols.ui.classes

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import org.teamseven.ols.databinding.FragmentAddMemberBinding


class AddMemberFragment : Fragment() {

    private lateinit var binding : FragmentAddMemberBinding
    private lateinit var navController : NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater)
        navController = findNavController()

        binding.btnAddMemberCopy.setOnClickListener {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("classCode", binding.textAddMemberClassCode.toString())
            clipboard.setPrimaryClip(clip)

            //Toast the copy
            //event log: System clipboard is unavailable
            Toast.makeText(requireContext(), "The class code is copied", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}