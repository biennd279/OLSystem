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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentNewMessageBinding
import org.teamseven.ols.entities.User
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import org.teamseven.ols.viewmodel.MessageViewModel


class NewMessageFragment : Fragment() {

    private val args by navArgs<NewMessageFragmentArgs>()
    private val classroomViewModel by activityViewModels<ClassroomViewModel>()
    private val messageViewModel by activityViewModels<MessageViewModel>()

    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var navController: NavController


    private lateinit var recyclerViewContacts: RecyclerView
    private var mAdapterContacts: ContactAdapter? = null

    private lateinit var recyclerViewSelectedContacts: RecyclerView
    private var mAdapterSelectedContacts: SelectedContactAdapter? = null

    private var selectedContactItems: MutableList<User> = mutableListOf()

    private lateinit var btnClear: ImageButton
    private lateinit var textContactSearch: EditText

    private val _classroomId by lazy { args.classroomId }

    private val _members: MutableLiveData<List<User>> = MutableLiveData()


    init {
        lifecycleScope.launchWhenResumed {
            classroomViewModel.students(_classroomId).observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.LOADING -> {

                    }

                    Resource.Status.SUCCESS -> {
                        if (it.data != null) {
                            _members.value = it.data
                            mAdapterContacts?.notifyDataSetChanged()
                        }
                    }

                    Resource.Status.ERROR -> {

                    }
                }
            }
        }
    }

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
        recyclerViewSelectedContacts.layoutManager = LinearLayoutManager(
            activity ,
            LinearLayoutManager.HORIZONTAL,
            false)
        mAdapterSelectedContacts = SelectedContactAdapter(selectedContactItems) {
            setSelectedContactItemsListener(it)
        }
        recyclerViewSelectedContacts.adapter = mAdapterSelectedContacts
        mAdapterSelectedContacts?.notifyDataSetChanged()
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

        recyclerViewContacts.layoutManager = LinearLayoutManager(activity)

        _members.observe(viewLifecycleOwner) {
            mAdapterContacts = ContactAdapter(it, selectedContactItems) {
                setContactItemsListener(it)
            }
            recyclerViewContacts.adapter = mAdapterContacts
        }
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

}