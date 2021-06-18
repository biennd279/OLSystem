package org.teamseven.ols.ui.conversation.message_box

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.MainActivity
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentMessageBoxBinding
import org.teamseven.ols.ui.classes.tabs.message.MessageItem
import org.teamseven.ols.ui.conversation.new_message.SelectedContactAdapter


class MessageBoxFragment : Fragment() {

    private lateinit var binding: FragmentMessageBoxBinding
    private lateinit var navController: NavController
    private var conversationItems: MutableList<ConversationItem> = mutableListOf()

    private lateinit var recyclerViewConversations: RecyclerView
    private var mAdapterConversations: ConversationAdapter? = null

    private lateinit var btn_send: ImageButton
    private lateinit var eText_new_message: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set AppBarTitle = conversationName
        (activity as MainActivity).setAppBarTitle("conversation name")


        // Inflate the layout for this fragment
        binding = FragmentMessageBoxBinding.inflate(inflater)
        navController = findNavController()

        //MessageBox recyclerview - load sent messages - conversation detail
        setRecyclerViewConversations()

        //Show, hide Send icon, Send icon click
        setSendMessageListener()


        return binding.root
    }

    private fun setSendMessageListener() {
        btn_send = binding.imgbtnMessageBoxSend
        eText_new_message = binding.edittxtMessageBoxNewMessage

        btn_send.setOnClickListener {
            //sendData to server and database
            //add to ConversationItems - list

            conversationItems.add(
                ConversationItem(
                    15,
                    "The quick brown fox jumps over the lazy dog \\n The quick brown fox jumps over the lazy dog",
                    0,
                    "ahihi",
                    R.drawable.ic_launcher_background,
                    "August 15, 2021",
                    "18:00"
                )
            )
            conversationItems.add(
                ConversationItem(
                    15,
                    "Nah",
                    0,
                    "ahihi",
                    R.drawable.ic_action_cancel_fill,
                    "August 15, 2021",
                    "18:00"
                )
            )

            conversationItems.add(
                ConversationItem(
                    15,
                    "The quick brown fox jumps over the lazy dog.",
                    1,
                    "ahihi",
                    R.drawable.ic_person_outline,
                    "August 15, 2021",
                    "18:00"
                )
            )

            conversationItems.add(
                ConversationItem(
                    15,
                    "Okay",
                    1,
                    "ahihi",
                    R.drawable.ic_action_cancel,
                    "August 15, 2021",
                    "18:00"
                )
            )
            mAdapterConversations?.notifyDataSetChanged()
            eText_new_message.text.clear()
        }

        eText_new_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    btn_send.visibility = View.INVISIBLE
                } else {
                    btn_send.visibility = View.VISIBLE
                }
            }
        })

    }

    private fun setRecyclerViewConversations() {
        recyclerViewConversations = binding.recyclerMessageBox
        recyclerViewConversations.layoutManager = LinearLayoutManager(activity , LinearLayoutManager.VERTICAL, false)

        getConversationMessages()
        mAdapterConversations = activity?.let {
            ConversationAdapter(it, conversationItems) {
                val toast = Toast.makeText(activity, it.sender_name, Toast.LENGTH_LONG)
                toast.show()

                Log.e("check_recycler_item_listener", it.sender_name)
            }
        }

        recyclerViewConversations.adapter = mAdapterConversations

        //recyclerViewConversations?.notifyDataSetChanged()

    }

    private fun getConversationMessages() {
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val username = resources.getStringArray(R.array.username)
        val message = resources.getStringArray(R.array.status)
        val time = resources.getStringArray(R.array.time)
        val date = resources.getStringArray(R.array.upload_date)

        //add to the bottom of the list - index = len(list)
        conversationItems.clear()
        //for (i in message.indices) {
            val i = 0
            conversationItems.add(
                ConversationItem(
                    i,
                    message[i],
                    i % 2,
                    username[i],
                    avatar.getResourceId(0, 0),
                    date[0],
                    time[0]
                )
            )
        //}


    }

}