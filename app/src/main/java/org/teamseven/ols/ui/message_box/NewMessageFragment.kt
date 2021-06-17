package org.teamseven.ols.ui.message_box

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentNewMessageBinding


class NewMessageFragment : Fragment() {

    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var navController: NavController
    private var contactItems: MutableList<ContactItem> = mutableListOf()
    private var mAdapter: ContactAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewMessageBinding.inflate(inflater)
        navController = findNavController()

        //recyclerView
        val recyclerView = binding.recyclerContactList

        //call func from PeopleViewModel to get all the file information
        //this is a test, remove it latter
        //but i dont know, what happen when click the people item - member
        getContactList()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter = activity?.let {
            ContactAdapter(it, contactItems) {
                val toast = Toast.makeText(activity, it.contact_name, Toast.LENGTH_LONG)
                toast.show()

                Log.e("check_recycler_item_listener", "clicked")
            }
        }
        recyclerView.adapter = mAdapter


        binding.edittextContactSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAdapter?.filter?.filter(s)
            }


        })
        return binding.root
    }

    private fun getContactList(){
        val username = resources.getStringArray(R.array.username)
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val classname = resources.getStringArray(R.array.classes_joined)

        contactItems.clear()
        for (i in username.indices) {
            contactItems.add(
                ContactItem(
                    0,
                    username[i],
                    1,
                    avatar.getResourceId(i, 0),
                )
            )
        }

        for (i in classname.indices) {
            contactItems.add(
                ContactItem(
                    0,
                    classname[i],
                    0,
                    avatar.getResourceId(i, 0),
                )
            )
        }
    }
}