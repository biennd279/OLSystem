package org.teamseven.ols.ui.classes.tabs.class_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.teamseven.ols.databinding.FragmentClassSettingBinding


class ClassSettingFragment : Fragment() {

    private lateinit var binding: FragmentClassSettingBinding
    private lateinit var mClassName: TextView
    private lateinit var mClassCode: TextView
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
        binding = FragmentClassSettingBinding.inflate(inflater)

        //get and edit with the right class name and class code
        //through call func in ViewModel with classId
        mClassName = binding.edittextSettingClassName
        mClassCode = binding.edittextSettingClassCode


        return binding.root
    }

    companion object {
        val TAG = ClassSettingFragment::class.java.simpleName

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int): ClassSettingFragment {
            val classSettingFragment = ClassSettingFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            classSettingFragment.arguments = args
            return classSettingFragment
        }

    }
}