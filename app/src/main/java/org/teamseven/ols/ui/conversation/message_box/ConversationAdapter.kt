package org.teamseven.ols.ui.conversation.message_box

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.databinding.ItemReceivedMessageBinding
import org.teamseven.ols.databinding.ItemSentMessageBinding
import org.teamseven.ols.entities.db.MessageWithSender
import java.text.SimpleDateFormat

class ConversationAdapter(
    private var conversationItems: List<MessageWithSender>,
    private val listener: (MessageWithSender) -> Unit,
    private val currentUserId: Int
) : ListAdapter<MessageWithSender, RecyclerView.ViewHolder>(MessageCallback()) {

    companion object {
        enum class TypeOfMessage {
            SENT, RECEIVED
        }
    }

    class SentMessageViewHolder(
        private val binding: ItemSentMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val username = binding.textConvItemUsernameYou
        private val message = binding.textConvItemMessageYou
        private val time = binding.textConvItemTimestampYou
        private val date = binding.textConvItemDateYou
        private val avatar = binding.imgConvItemProfileYou

        fun bindItem(items: MessageWithSender, listener: (MessageWithSender) -> Unit) {
            username.text = items.sender.name
            message.text = items.message.messageText
            items.message.createdAt?.let {
                time.text = SimpleDateFormat.getTimeInstance().format(items.message.createdAt)
                date.text = SimpleDateFormat.getDateInstance().format(items.message.createdAt)
            }

            if (items.sender.avatarUrl != null) {
                Glide.with(itemView.context).load(items.sender.avatarUrl).into(avatar)
            }

            itemView.setOnClickListener {
                listener(items)
            }
        }

        companion object {
            fun from(parent: ViewGroup): SentMessageViewHolder {
                val binding = ItemSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SentMessageViewHolder(binding)
            }
        }
    }

    class ReceivedMessageViewHolder(
        private val binding: ItemReceivedMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val username = binding.textConvItemUsernameOther
        private val message = binding.textConvItemMessageOther
        private val time = binding.textConvItemTimestampOther
        private val date = binding.textConvItemDateOther
        private val avatar = binding.imgConvItemProfileOther

        fun bindItem(items: MessageWithSender, listener: (MessageWithSender) -> Unit) {
            username.text = items.sender.name
            message.text = items.message.messageText
            items.message.createdAt?.let {
                time.text = SimpleDateFormat.getTimeInstance().format(items.message.createdAt)
                date.text = SimpleDateFormat.getDateInstance().format(items.message.createdAt)
            }

            if (items.sender.avatarUrl != null) {
                Glide.with(itemView.context).load(items.sender.avatarUrl).into(avatar)
            }

            itemView.setOnClickListener {
                listener(items)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ReceivedMessageViewHolder {
                val binding = ItemReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ReceivedMessageViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TypeOfMessage.SENT.ordinal -> {
                SentMessageViewHolder.from(parent)
            }
            else -> {
                ReceivedMessageViewHolder.from(parent)
            }
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            TypeOfMessage.SENT.ordinal -> {
                val correctViewHolder = viewHolder as SentMessageViewHolder
                correctViewHolder.bindItem(conversationItems[position], listener)
            }
            else -> {
                val correctViewHolder = viewHolder as ReceivedMessageViewHolder
                correctViewHolder.bindItem(conversationItems[position], listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (conversationItems[position].sender.id == currentUserId)
            TypeOfMessage.SENT.ordinal else TypeOfMessage.RECEIVED.ordinal
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = conversationItems.size
}

class MessageCallback : DiffUtil.ItemCallback<MessageWithSender>() {
    override fun areItemsTheSame(oldItem: MessageWithSender, newItem: MessageWithSender): Boolean {
        return oldItem.message.id == newItem.message.id
    }

    override fun areContentsTheSame(
        oldItem: MessageWithSender,
        newItem: MessageWithSender
    ): Boolean {
        return oldItem == newItem
    }

}