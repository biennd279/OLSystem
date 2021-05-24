package org.teamseven.ols.ui.course_content

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R

class MessageAdapter(private val context: Context, private  val messageItems: List<MessageItem>, private val listener: (MessageItem) -> Unit)
    : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item_view, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(messageItems[position], listener)
    }

    override fun getItemCount(): Int =messageItems.size

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
}