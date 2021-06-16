package org.teamseven.ols.ui.classes.tabs.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentPeopleBinding


class PeopleFragment : Fragment() {

    private lateinit var binding: FragmentPeopleBinding
    private lateinit var navController: NavController
    private var peopleItems: MutableList<PeopleItem> = mutableListOf()
    private var totalMembers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPeopleBinding.inflate(inflater)
        navController = findNavController()

        //recyclerView
        val recyclerView = binding.recyclerMemberList
        getPeopleList()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = activity?.let {
            PeopleAdapter(it, peopleItems) {
                val toast = Toast.makeText(activity, it.username, Toast.LENGTH_LONG)
                toast.show()
            }
        }

        binding.textPeopleTotalMembers.text = totalMembers.toString()

        //button add member listener
        binding.btnAddMember.setOnClickListener{
            navController.navigate(R.id.addMemberFragment)
        }

        return binding.root
    }

    companion object {
        val TAG = PeopleFragment::class.java.simpleName

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int): PeopleFragment {
            val peopleFragment = PeopleFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            peopleFragment.arguments = args
            return peopleFragment
        }
    }

    private fun getPeopleList(){
        val username = resources.getStringArray(R.array.username)
        val avatar = resources.obtainTypedArray(R.array.avatar)
        totalMembers = username.size

        peopleItems.clear()
        for (i in username.indices) {
            peopleItems.add(
                PeopleItem(
                    username[i],
                    avatar.getResourceId(i, 0),
                )
            )
        }
    }
}