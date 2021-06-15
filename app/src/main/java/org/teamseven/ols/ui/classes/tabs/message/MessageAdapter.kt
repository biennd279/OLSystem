package org.teamseven.ols.ui.classes.tabs.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R

class MessageAdapter(
    private val context: Context,
    private val messageItems: List<MessageItem>,
    private val listener: (MessageItem) -> Unit
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    //ViewHolder
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.username)
        val status = view.findViewById<TextView>(R.id.status)
        val avatar = view.findViewById<ImageView>(R.id.avatar)
        val time = view.findViewById<TextView>(R.id.time)

        fun bindItem(items: MessageItem, listener: (MessageItem) -> Unit) {
            username.text = items.username
            status.text = items.status
            time.text = items.time

            Glide.with(itemView.context).load(items.avatar).into(avatar)
            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message_view, parent, false)
        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(messageItems[position], listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messageItems.size

}