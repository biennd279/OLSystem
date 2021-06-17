package org.teamseven.ols.ui.classes.tabs.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R

class PeopleAdapter(
    private val context: Context,
    private val peopleItems: List<PeopleItem>,
    private val listener: (PeopleItem) -> Unit
) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {


    //ViewHolder
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.text_member_name)
        val avatar = view.findViewById<ImageView>(R.id.img_member_avatar)

        fun bindItem(items: PeopleItem, listener: (PeopleItem) -> Unit) {
            username.text = items.username

            Glide.with(itemView.context).load(items.avatar).into(avatar)
            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_member_view, parent, false)
        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(peopleItems[position], listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = peopleItems.size

}