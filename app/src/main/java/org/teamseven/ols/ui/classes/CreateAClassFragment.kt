package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentCreateAClassBinding
import org.teamseven.ols.entities.requests.ClassroomInfoRequest
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import timber.log.Timber


class CreateAClassFragment : Fragment() {

    private lateinit var binding : FragmentCreateAClassBinding
    private lateinit var navController: NavController

    private val classroomViewModel by activityViewModels<ClassroomViewModel>()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateAClassBinding.inflate(inflater)
        navController = findNavController()

        binding.btnCreateClass.setOnClickListener {
            // get className and classSchool
            //binding.edittextCreateClassName.text
            //binding.edittextCreateClassSchool.text

            //call the func from CreateAClassViewModel
            //create the class with className and classScholl (with the classId)
            //success -> add new className in classesOwned List in MainActivity
            //load the new class
            val classroomName = binding.edittextCreateClassName.text.toString()
            val classroomSchool = binding.edittextCreateClassSchool.text.toString()
            if (classroomName.isNotBlank() && classroomSchool.isNotBlank()) {
                classroomViewModel.createClassroom(
                    ClassroomInfoRequest(
                        classroomName,
                        classroomSchool
                    )
                ).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Resource.Status.LOADING -> {

                        }

                        Resource.Status.SUCCESS -> {
                            (activity as MainActivity).onCreateClassroom(
                                it.data ?: -1,
                                classroomName
                            )
                            navController.navigate(
                                CreateAClassFragmentDirections.actionCreateAClassFragmentToHomeFragment()
                            )
                            Toast.makeText(
                                context,
                                "Created success, check in class owned list",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        Resource.Status.ERROR -> {
                            Timber.i(it.message)
                            Toast.makeText(
                                context,
                                "Some thing when wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

        }
        return binding.root
    }

}