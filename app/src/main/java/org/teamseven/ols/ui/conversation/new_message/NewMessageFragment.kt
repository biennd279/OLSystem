package org.teamseven.ols.ui.conversation.new_message

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentNewMessageBinding
import org.teamseven.ols.entities.User


class NewMessageFragment : Fragment() {

    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var navController: NavController


    private lateinit var recyclerViewContacts: RecyclerView
    private var mAdapterContacts: ContactAdapter? = null

    private lateinit var recyclerViewSelectedContacts: RecyclerView
    private var mAdapterSelectedContacts: SelectedContactAdapter? = null

    private var contactItems: MutableList<User> = mutableListOf()
    private var selectedContactItems: MutableList<User> = mutableListOf()

    private lateinit var btnClear: ImageButton
    private lateinit var textContactSearch: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewMessageBinding.inflate(inflater)
        navController = findNavController()

        btnClear = binding.imgbtnNewMessageClear
        textContactSearch = binding.edittextContactSearch


        // selected contacts recyclerView
        setRecyclerViewSelectedContacts()

        // contacts recyclerView
        setRecyclerViewContacts()


        //set event listener for clear button and eddittex search
        setEventListenerForSearch()

        //set event listener when send button clicked
        setEventListenerForSent()

        return binding.root
    }

    private fun setEventListenerForSent() {
        binding.imgbtnNewMessageSend.setOnClickListener {

            // + navigate with sending new conversation data
//            navController.navigate(NewMessageFragmentDirections.actionNewMessageFragmentToMessageBoxFragment())
        }
    }

    private fun setRecyclerViewSelectedContacts() {
        recyclerViewSelectedContacts = binding.recyclerSelectedContactList

        //call func from PeopleViewModel to get all the file information
        //this is a test, remove it latter
        //but i dont know, what happen when click the people item - member


        recyclerViewSelectedContacts.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL, false)
        mAdapterSelectedContacts = activity?.let {
            SelectedContactAdapter(it, selectedContactItems) {
                val toast = Toast.makeText(
                    activity,
                    it.name,
                    Toast.LENGTH_LONG
                )
                toast.show()


                setSelectedContactItemsListener(it)
            }
        }
        recyclerViewSelectedContacts.adapter = mAdapterSelectedContacts
        getSelectedContactList()
        mAdapterSelectedContacts?.notifyDataSetChanged()
    }

    private fun getSelectedContactList() {
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val classname = resources.getStringArray(R.array.classes_joined)

        selectedContactItems.clear()
        /*
        for (i in classname.indices) {
            selectedContactItems.add(
                ContactItem(
                    0,
                    classname[i],
                    0,
                    avatar.getResourceId(i, 0),
                )
            )
        }*/
    }

    private fun setSelectedContactItemsListener(contactItem : User) {
        //remove from selectedContactItems
        //add back contactItems

        selectedContactItems.remove(contactItem)
        mAdapterSelectedContacts?.notifyDataSetChanged()

        mAdapterContacts?.setContactItemsFiltered()
        mAdapterContacts?.notifyDataSetChanged()

        //if no selected contact hide the new message box
        if (selectedContactItems.isEmpty()) {
            binding.rlayoutNewMessageChatbox.visibility = View.GONE
        }
    }

    private fun setEventListenerForSearch() {

        //clear button
        btnClear.setOnClickListener{
            textContactSearch.text.clear()
        }


        textContactSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAdapterContacts?.filter?.filter(s)
                if (s.isEmpty()) {
                    btnClear.visibility = View.INVISIBLE
                } else {
                    btnClear.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setRecyclerViewContacts() {
        recyclerViewContacts = binding.recyclerContactList

        //call func from PeopleViewModel to get all the file information
        //this is a test, remove it latter
        //but i dont know, what happen when click the people item - member
        getContactList()

        recyclerViewContacts.layoutManager = LinearLayoutManager(activity)
        mAdapterContacts = activity?.let {
            ContactAdapter(it, contactItems, selectedContactItems) {
                val toast = Toast.makeText(activity, it.name, Toast.LENGTH_LONG)
                toast.show()

                Log.e("check_recycler_item_listener", "clicked")

                setContactItemsListener(it)
            }
        }
        recyclerViewContacts.adapter = mAdapterContacts

    }

    private fun setContactItemsListener(contactItem: User) {
        //when click remove from the contact list
        //add to the selected contact

        selectedContactItems.add(contactItem)
        mAdapterSelectedContacts?.notifyDataSetChanged()

        mAdapterContacts?.setContactItemsFiltered()
        mAdapterContacts?.notifyDataSetChanged()

        //if there are selected contacts, show the new message box
        if (!selectedContactItems.isEmpty()) {
            binding.rlayoutNewMessageChatbox.visibility = View.VISIBLE
        }
    }

    private fun getContactList(){
        val username = resources.getStringArray(R.array.username)
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val classname = resources.getStringArray(R.array.classes_joined)

        //People + group chat (class group chat + other (> 2, = 1 -> people))
    }
}