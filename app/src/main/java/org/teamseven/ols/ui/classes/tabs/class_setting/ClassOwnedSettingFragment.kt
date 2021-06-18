package org.teamseven.ols.ui.classes.tabs.class_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import org.teamseven.ols.databinding.FragmentClassOwnedSettingBinding
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import timber.log.Timber
import kotlin.properties.Delegates


class ClassOwnedSettingFragment : Fragment() {

    private lateinit var binding: FragmentClassOwnedSettingBinding
    private lateinit var mClassName: TextView
    private lateinit var mClassCode: TextView
    private lateinit var mClassSchool: TextView
    private var mtab by Delegates.notNull<Int>()
    private var mClassId by Delegates.notNull<Int>()
    private val classroomViewModel: ClassroomViewModel by activityViewModels()

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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClassOwnedSettingBinding.inflate(inflater)

        //get and edit with the right class code, class name and class school
        //through call func in ViewModel with classId
        //this just for now, remove it later
        setUpClassInformation()

        // Change and Update class info (name, shool, the code is unchangeable)
        binding.btnSettingClassOwnedSave.setOnClickListener {
            // call function in ClassOwnedSettingViewModel (ClassSettingViewModel)
            // update in server and database
            // update in classesOwned <List> from MainActivity (write a  function and call it)
            // reload the classFragment by call setUpCurrentClass func
        }

        // delete the class
        binding.btnSettingClassOwnedDelete.setOnClickListener {
            // call function in ClassOwnedSettingViewModel (ClassSettingViewModel)
            // delete in server and database
            // delete in classesOwned <List> from MainActivity (write a  function and call it)
            // change the classFragment to AllClassesFragment
            // by call onNavigationItemSelected(navigationView.menu.findItem(R.id.item_all_classes)) (write a func)
            // or change the currentClassId = -1 and reload by setUpCurrentClass func
        }


        return binding.root
    }

    private fun setUpClassInformation() {
        mClassCode = binding.edittextSettingClassOwnedCode
        mClassName = binding.edittextSettingClassOwnedName
        mClassSchool = binding.edittextSettingClassOwnedSchool

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
        val TAG = ClassOwnedSettingFragment::class.java.simpleName

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int): ClassOwnedSettingFragment {
            val classSettingFragment = ClassOwnedSettingFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            classSettingFragment.arguments = args
            return classSettingFragment
        }

    }
}