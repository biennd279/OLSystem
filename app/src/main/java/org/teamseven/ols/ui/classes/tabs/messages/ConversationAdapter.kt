package org.teamseven.ols.ui.classes.tabs.messages

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R
import org.teamseven.ols.databinding.ItemMessageViewBinding
import org.teamseven.ols.entities.Conversation

class ConversationAdapter(
    private val conversations: List<Conversation>,
    private val listener: (Conversation) -> Unit
) : ListAdapter<Conversation, RecyclerView.ViewHolder>(MessageCallback()) {


    //ViewHolder
    class ViewHolder private constructor(
        private val binding: ItemMessageViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val username = binding.imgMessageUsername
        val status = binding.textMessageStatus
        val avatar = binding.imgMessageAvatar
        val time = binding.textMessageTime

        @SuppressLint("ResourceType")
        fun bind(items: Conversation, listener: (Conversation) -> Unit) {
            username.text = if (items.name.isNullOrEmpty()) "A Convesation" else items.name
            status.text = items.type
            time.text = items.id.toString()

            Glide.with(itemView.context).load(R.drawable.ic_class_icon).into(avatar)

            itemView.setOnClickListener{
                listener(items)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemMessageViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ConversationAdapter.ViewHolder(binding)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(conversations[position], listener)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = conversations.size


}

class MessageCallback(): DiffUtil.ItemCallback<Conversation>() {
    override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem == newItem
    }


}