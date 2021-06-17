package org.teamseven.ols.ui.classes.tabs.people

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentPeopleBinding
import org.teamseven.ols.entities.User
import org.teamseven.ols.utils.Resource
import timber.log.Timber


class PeopleFragment private constructor(
    private val members: LiveData<Resource<List<User>>>
    ): Fragment() {

    private lateinit var binding: FragmentPeopleBinding
    private lateinit var navController: NavController
    private var peopleItems: List<PeopleItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPeopleBinding.inflate(inflater)
        navController = findNavController()

        getPeopleList()

        //button add member listener
        binding.btnAddMember.setOnClickListener{
            navController.navigate(R.id.addMemberFragment)
        }

        return binding.root
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int, members: LiveData<Resource<List<User>>>): PeopleFragment {
            val peopleFragment = PeopleFragment(members)
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            peopleFragment.arguments = args
            return peopleFragment
        }
    }

    @SuppressLint("Recycle")
    private fun getPeopleList(){
        val recyclerView = binding.recyclerMemberList

        recyclerView.layoutManager = LinearLayoutManager(activity)

       members.observe(viewLifecycleOwner) {
           when (it.status) {
               Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                   if (it.data.isNullOrEmpty()) {
                       return@observe
                   }

                   peopleItems = it.data.map {
                       PeopleItem(
                           it.name,
                           avatar = resources.obtainTypedArray(R.array.avatar).getResourceId(0, 0)
                       )
                   }

                   recyclerView.adapter = activity?.let {
                       PeopleAdapter(it, peopleItems) {
                           val toast = Toast.makeText(activity, it.username, Toast.LENGTH_LONG)
                           toast.show()
                       }
                   }

                   binding.textPeopleTotalMembers.text = it.data.size.toString()
               }
               Resource.Status.ERROR -> Timber.i(it.message)
           }
       }
    }
}