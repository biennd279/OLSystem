package org.teamseven.ols.ui.classes.tabs.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentMessagesBinding


class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var navController: NavController
    private var mTab = 0
    private var mClassId = -1
    private var messageItems: MutableList<MessageItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTab = requireArguments().getInt("tab", 0)
        mClassId = requireArguments().getInt("classId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(inflater)
        navController = findNavController()

        //recyclerView
        val recyclerView = binding.recyclerMessageList

        //call func from MessageViewModel to get all the file information (the MessageBox for chatting, this is for show all messages-people-group)
        //this is a test, remove it latter
        //when click the item, open in message box. (i've not do anything with message box yet)
        getMessageList()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = activity?.let {
            MessageAdapter(it, messageItems) {
                val toast = Toast.makeText(activity, it.username, Toast.LENGTH_LONG)
                toast.show()

                //set navigate to message box
                setMessageItemClickListener(it)
            }
        }


        binding.btnNewMessage.setOnClickListener {
            //navigate the new message fragment
            //have not created
            navController.navigate(R.id.newMessageFragment)
        }

        return binding.root
    }

    private fun setMessageItemClickListener(it: MessageItem) {
        navController.navigate(R.id.messageBoxFragment)
    }

    companion object {
        val TAG = MessagesFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(tab: Int, classId: Int): MessagesFragment {
            val messagesFragment = MessagesFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            messagesFragment.arguments = args
            return messagesFragment
        }
    }


    @SuppressLint("Recycle")
    private fun getMessageList(){
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val username = resources.getStringArray(R.array.username)
        val status = resources.getStringArray(R.array.status)
        val time = resources.getStringArray(R.array.time)

        messageItems.clear()
        for (i in username.indices) {
            messageItems.add(
                MessageItem(
                    username[i],
                    status[i],
                    avatar.getResourceId(i, 0),
                    time[i]
                )
            )
        }
    }

}
