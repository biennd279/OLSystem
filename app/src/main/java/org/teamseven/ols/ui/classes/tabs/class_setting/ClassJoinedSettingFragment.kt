package org.teamseven.ols.ui.classes.tabs.class_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.teamseven.ols.MainActivity
import org.teamseven.ols.databinding.FragmentClassJoinedSettingBinding
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import timber.log.Timber
import kotlin.properties.Delegates


class ClassJoinedSettingFragment : Fragment() {

    private lateinit var binding: FragmentClassJoinedSettingBinding
    private lateinit var mClassName: TextView
    private lateinit var mClassCode: TextView
    private lateinit var mClassSchool: TextView
    private var mtab by Delegates.notNull<Int>()
    private var mClassId by Delegates.notNull<Int>()
    private lateinit var classroomViewModel: ClassroomViewModel
    private lateinit var onLeaveClass: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mtab = it.getInt("tab")
            mClassId = it.getInt("classId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassJoinedSettingBinding.inflate(inflater)

        //get and edit with the right class code, class name and class school
        //through call func in ViewModel with classId
        //this just for now, remove it later
        setUpClassInformation()

        // leave the class
        binding.btnSettingClassJoinedLeave.setOnClickListener {
            // call function in ClassOwnedSettingViewModel (ClassSettingViewModel)
            // delete in server and database
            // delete in classesJoined <List> from MainActivity (write a  function and call it)
            // change the classFragment to AllClassesFragment
            // by call onNavigationItemSelected(navigationView.menu.findItem(R.id.item_all_classes)) (write a func)
            // or change the currentClassId = -1 and reload by setUpCurrentClass func

            classroomViewModel.leaveClass(mClassId)
                .observe(viewLifecycleOwner) {
                    when (it.status) {
                        Resource.Status.LOADING -> {

                        }

                        Resource.Status.SUCCESS -> {
                            (activity as MainActivity).onLeaveClassroom()
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

        return binding.root
    }

    private fun setUpClassInformation() {
        mClassCode = binding.edittextSettingClassJoinedCode
        mClassName = binding.edittextSettingClassJoinedName
        mClassSchool = binding.edittextSettingClassJoinedSchool

        classroomViewModel.classroomInfo(mClassId)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                        if (it.data == null) {
                            Timber.i(it.message)
                            return@observe
                        }

                        mClassCode.text = it.data.code
                        mClassName.text = it.data.name
                        mClassSchool.text = it.data.school
                    }

                    Resource.Status.ERROR -> Timber.i(it.message)
                }
            }

    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(
            tab: Int,
            classId: Int,
            classroomViewModel: ClassroomViewModel,
        ): ClassJoinedSettingFragment {
            val classSettingFragment = ClassJoinedSettingFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            classSettingFragment.arguments = args
            classSettingFragment.classroomViewModel = classroomViewModel
            return classSettingFragment
        }

    }
}