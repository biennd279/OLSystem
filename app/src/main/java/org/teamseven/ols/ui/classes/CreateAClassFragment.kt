package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentCreateAClassBinding


class CreateAClassFragment : Fragment() {

    private lateinit var binding : FragmentCreateAClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateAClassBinding.inflate(inflater)

        binding.btnCreateClass.setOnClickListener {
            // get className and classSchool
            //binding.edittextCreateClassName.text
            //binding.edittextCreateClassSchool.text

            //call the func from CreateAClassViewModel
            //create the class with className and classScholl (with the classId)
            //success -> add new className in classesOwned List in MainActivity
            //load the new class

        }
        return binding.root
    }

}