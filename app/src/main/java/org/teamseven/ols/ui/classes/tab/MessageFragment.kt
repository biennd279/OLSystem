package org.teamseven.ols.ui.classes.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentMessageBinding


class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
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
        binding = FragmentMessageBinding.inflate(inflater)

        //recyclerView
        val recyclerView = binding.messageList
        getMessageList()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = activity?.let {
            MessageAdapter(it, messageItems) {
                val toast = Toast.makeText(activity, it.username, Toast.LENGTH_LONG)
                toast.show()
            }
        }


        return binding.root
    }

    companion object {
        val TAG = MessageFragment::class.java.simpleName

        fun newInstance(tab: Int, classId: Int): MessageFragment {
            val messageFragment = MessageFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            messageFragment.arguments = args
            return messageFragment
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
