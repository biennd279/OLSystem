package org.teamseven.ols.ui.classes.all_classes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentAllClassesBinding
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.viewmodel.MessageViewModel


class AllClassesFragment : Fragment() {

    private lateinit var binding: FragmentAllClassesBinding
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
        binding = FragmentAllClassesBinding.inflate(inflater)
        navController = findNavController()

        val sectionsPagerAdapter = AllClassesSectionsPagerAdapter(
            requireContext(),
            childFragmentManager,
            mClassId
        )
        val viewPager: ViewPager = binding.allClassesViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.allClassesTabs
        tabs.setupWithViewPager(viewPager)



        //menu
        //setHasOptionsMenu(true)

        return binding.root
    }

    companion object {

        fun newInstance(
            classId: Int,
            className: String
        ): AllClassesFragment {
            val allClassesFragment = AllClassesFragment()
            val args = Bundle()
            args.putString("className", className)
            args.putInt("classId", classId)
            allClassesFragment.arguments = args
            return allClassesFragment
        }
    }

}