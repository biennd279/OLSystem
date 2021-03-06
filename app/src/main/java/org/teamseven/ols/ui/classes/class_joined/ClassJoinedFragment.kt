package org.teamseven.ols.ui.classes.class_joined

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import org.teamseven.ols.databinding.FragmentClassBinding
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.viewmodel.ClassroomViewModel
import org.teamseven.ols.viewmodel.MessageViewModel


class ClassJoinedFragment : Fragment() {

    private lateinit var binding: FragmentClassBinding
    private lateinit var navController: NavController
    private var mClassName: String = ""
    private var mClassId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClassId = requireArguments().getInt("classId")
        mClassName = requireArguments().getString("className", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentClassBinding.inflate(inflater)
        navController = findNavController()

        val sectionsPagerAdapter = ClassJoinedSectionsPagerAdapter(
            requireContext(),
            childFragmentManager,
            mClassId
        )
        val viewPager: ViewPager = binding.classViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.classTabs
        tabs.setupWithViewPager(viewPager)

        //menu
        //setHasOptionsMenu(true)

        return binding.root
    }

    companion object {
        fun newInstance(
            classId: Int,
            className: String
        ): ClassJoinedFragment {
            val classFragment = ClassJoinedFragment()
            val args = Bundle()
            args.putString("className", className)
            args.putInt("classId", classId)
            classFragment.arguments = args
            return classFragment
        }
    }

}