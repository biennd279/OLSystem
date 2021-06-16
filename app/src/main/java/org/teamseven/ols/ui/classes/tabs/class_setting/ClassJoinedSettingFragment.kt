package org.teamseven.ols.ui.classes.tabs.class_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.teamseven.ols.databinding.FragmentClassJoinedSettingBinding


class ClassJoinedSettingFragment : Fragment() {

    private lateinit var binding: FragmentClassJoinedSettingBinding
    private lateinit var mClassName: TextView
    private lateinit var mClassCode: TextView
    private lateinit var mClassSchool: TextView
    private var mtab: Int? = null
    private var mClassId: Int? = null

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
        }

        return binding.root
    }

    private fun setUpClassInformation() {
        mClassCode = binding.edittextSettingClassJoinedCode
        mClassName = binding.edittextSettingClassJoinedName
        mClassSchool = binding.edittextSettingClassJoinedSchool

        //call func in ViewModel to get the infomation
        mClassCode.text = "123"
        mClassName.text = "justAClass"
        mClassSchool.text = "a some shool i dont know"

    }

    companion object {
        val TAG = ClassJoinedSettingFragment::class.java.simpleName

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int): ClassJoinedSettingFragment {
            val classSettingFragment = ClassJoinedSettingFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            classSettingFragment.arguments = args
            return classSettingFragment
        }

    }
}