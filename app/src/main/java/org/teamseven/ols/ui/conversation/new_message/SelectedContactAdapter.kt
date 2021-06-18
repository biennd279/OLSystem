package org.teamseven.ols.ui.conversation.new_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R

class SelectedContactAdapter(
    private val context: Context,
    private var SelectedContactItems: List<ContactItem>,
    private val listener: (ContactItem) -> Unit
) : RecyclerView.Adapter<SelectedContactAdapter.ViewHolder>() {

    //ViewHolder
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val selected_contact_name = view.findViewById<TextView>(R.id.text_selected_contact_name)

        fun bindItem(items: ContactItem, listener: (ContactItem) -> Unit) {
            selected_contact_name.text = items.contact_name

            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_selected_contact, parent, false)
        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(SelectedContactItems[position], listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = SelectedContactItems.size


}