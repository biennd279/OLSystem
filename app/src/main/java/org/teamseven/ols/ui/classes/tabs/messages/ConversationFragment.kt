package org.teamseven.ols.ui.classes.tabs.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentMessagesBinding
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.MessageViewModel
import timber.log.Timber


class ConversationFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var navController: NavController
    private var mTab = 0
    private var mClassId = -1
    private var _conversations: MutableLiveData<MutableList<Conversation>> = MutableLiveData()
    private lateinit var messageViewModel: MessageViewModel

    init {
        lifecycleScope.launchWhenResumed {
            refreshConversation()
        }
    }


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

        setUpUi()

        binding.btnNewMessage.setOnClickListener {
            //navigate the new message fragment
            //have not created
            navController.navigate(R.id.newMessageFragment)
        }

        return binding.root
    }

    companion object {
        fun newInstance(
            tab: Int,
            classId: Int,
            messageViewModel: MessageViewModel
        ): ConversationFragment {
            val messagesFragment = ConversationFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            messagesFragment.arguments = args
            messagesFragment.messageViewModel = messageViewModel
            return messagesFragment
        }
    }

    private fun setUpUi(listener:(Conversation) -> Unit = {}) {
        val recyclerView = binding.recyclerMessageList
        recyclerView.layoutManager = LinearLayoutManager(activity)

        _conversations.observe(viewLifecycleOwner) {
            recyclerView.adapter = ConversationAdapter(
                it,
                listener
            )
        }
    }


    private fun refreshConversation(){
        messageViewModel.conversations(mClassId)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                        if (it.data == null) {
                            return@observe
                        }

                        _conversations.value = it.data as MutableList<Conversation>?
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(
                            context,
                            "Can not load conversation",
                            Toast.LENGTH_LONG
                        ).show()
                        Timber.i(it.message)
                    }
                }
            }
    }

}
