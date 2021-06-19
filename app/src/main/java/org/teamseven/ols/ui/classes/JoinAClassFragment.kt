package org.teamseven.ols.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentJoinAClassBinding
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import timber.log.Timber

class JoinAClassFragment : Fragment() {

    private lateinit var binding: FragmentJoinAClassBinding

    private val classroomViewModel by activityViewModels<ClassroomViewModel>()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentJoinAClassBinding.inflate(inflater)

        binding.btnJoinClass.setOnClickListener {
            // get classCode
            //binding.edittextJoinClassCode.text

            //call the func from JoinAClassViewModel
            //join the class with classCode
            //success -> add new className in classesJoined List in MainActivity (with the classId)
            //load the new class
            if (binding.edittextJoinClassCode.text?.isNotBlank() == true) {
                classroomViewModel.joinClassroom(
                    binding.edittextJoinClassCode.text.toString()
                ).observe(viewLifecycleOwner) {
                    when (it.status) {
                        Resource.Status.LOADING -> {

                        }

                        Resource.Status.SUCCESS -> {
                            (activity as MainActivity).onJoinedClassroom(it.data ?: -1)
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