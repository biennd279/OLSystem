package org.teamseven.ols.ui.conversation.message_box

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R

class ConversationAdapter(
    private val context: Context,
    private var conversationItems: List<ConversationItem>,
    private val listener: (ConversationItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //ViewHolder for received other message
    class ViewHolderForOtherMessage(view: View): RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.text_conv_item_username_other)
        val message = view.findViewById<TextView>(R.id.text_conv_item_message_other)
        val time = view.findViewById<TextView>(R.id.text_conv_item_timestamp_other)
        val date = view.findViewById<TextView>(R.id.text_conv_item_date_other)
        val avatar = view.findViewById<ImageView>(R.id.img_conv_item_profile_other)

        fun bindItem(items: ConversationItem, listener: (ConversationItem) -> Unit) {
            username.text = items.sender_name
            message.text = items.message
            time.text = items.time
            date.text = items.date

            Glide.with(itemView.context).load(items.sender_avatar).into(avatar)

            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    //ViewHolder for your sent message
    class ViewHolderForYourMessage(view: View): RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.text_conv_item_username_you)
        val message = view.findViewById<TextView>(R.id.text_conv_item_message_you)
        val time = view.findViewById<TextView>(R.id.text_conv_item_timestamp_you)
        val date = view.findViewById<TextView>(R.id.text_conv_item_date_you)
        val avatar = view.findViewById<ImageView>(R.id.img_conv_item_profile_you)

        fun bindItem(items: ConversationItem, listener: (ConversationItem) -> Unit) {
            username.text = "You"   //items.sender_name
            message.text = items.message
            time.text = items.time
            date.text = items.date

            Glide.with(itemView.context).load(items.sender_avatar).into(avatar)

            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_sent_message, parent, false)
                return ViewHolderForYourMessage(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_received_message, parent, false)
                return ViewHolderForOtherMessage(view)
            }
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            0 -> {
                val correctViewHolder : ViewHolderForYourMessage = viewHolder as ViewHolderForYourMessage
                correctViewHolder.bindItem(conversationItems[position], listener)
            }
            else -> {
                val correctViewHolder : ViewHolderForOtherMessage = viewHolder as ViewHolderForOtherMessage
                correctViewHolder.bindItem(conversationItems[position], listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //change with userId
        //the sender is user
        when (conversationItems[position].sender_id) {
            0 -> {
                //viewholder for your message
                return 0
            }
            else -> {
                //viewholder for other message
                return 1
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = conversationItems.size


}