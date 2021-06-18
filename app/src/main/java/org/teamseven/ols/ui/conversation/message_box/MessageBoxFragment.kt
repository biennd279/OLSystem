package org.teamseven.ols.ui.conversation.message_box

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.MainActivity
import org.teamseven.ols.databinding.FragmentMessageBoxBinding
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.db.MessageWithSender
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.MessageViewModel
import timber.log.Timber


class MessageBoxFragment : Fragment() {
    private val args: MessageBoxFragmentArgs by navArgs()
    private val messageViewMode: MessageViewModel by activityViewModels()

    private lateinit var binding: FragmentMessageBoxBinding
    private lateinit var navController: NavController

    private lateinit var recyclerViewConversations: RecyclerView

    private lateinit var btnSend: ImageButton
    private lateinit var textNewMessage: EditText

    private lateinit var sessionManager: SessionManager

    private var _messages: MutableLiveData<MutableList<MessageWithSender>> = MutableLiveData()

    private var _members: MutableLiveData<List<User>> = MutableLiveData()

    init {
        lifecycleScope.launchWhenResumed {
            refreshMembers()
            refreshMessage()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBoxBinding.inflate(
            inflater,
            container,
            false
        )
        navController = findNavController()

        sessionManager = SessionManager(requireContext())

        setUpUi()

        (activity as MainActivity).setAppBarTitle("conversation name")

        return binding.root
    }

    private fun setUpUi() {
        btnSend = binding.imgbtnMessageBoxSend
        textNewMessage = binding.edittxtMessageBoxNewMessage

        btnSend.setOnClickListener {
            //sendData to server and database
            //add to ConversationItems - list

        }

        recyclerViewConversations = binding.recyclerMessageBox

        recyclerViewConversations.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            true
        )


        _messages.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerViewConversations.adapter = ConversationAdapter(
                    it,
                    {},
                    sessionManager.userId
                )
            }

        }

        textNewMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    btnSend.visibility = View.INVISIBLE
                } else {
                    btnSend.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun refreshMessage() {
        val conversationId = args.conversationId
        messageViewMode.messages(conversationId)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                        _messages.value = it.data as MutableList<MessageWithSender>?
                    }

                    Resource.Status.ERROR -> {
                        Timber.i(it.message)
                        Toast.makeText(
                            context,
                            "Can not load message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun refreshMembers() {
        val conversationId = args.conversationId
        messageViewMode.members(conversationId)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                        if (it != null) {
                            _members.value = it.data
                        }
                    }

                    Resource.Status.ERROR -> {
                        Timber.i(it.message)
                        Toast.makeText(
                            context,
                            "Can not load message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

}