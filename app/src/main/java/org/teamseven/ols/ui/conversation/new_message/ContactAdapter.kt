package org.teamseven.ols.ui.message_box.new_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private val context: Context,
    private var ContactItems: List<ContactItem>,
    private var selectedContactItems: List<ContactItem>,
    private val listener: (ContactItem) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {

    var ContactItemsFiltered: List<ContactItem>
    init {
        ContactItemsFiltered = ContactItems - selectedContactItems
    }

    fun setContactItemsFiltered() {
        ContactItemsFiltered = ContactItems - selectedContactItems
    }

    //ViewHolder
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val contact_name = view.findViewById<TextView>(R.id.text_contact_name)
        val contact_avatar = view.findViewById<ImageView>(R.id.img_contact_avatar)

        fun bindItem(items: ContactItem, listener: (ContactItem) -> Unit) {
            contact_name.text = items.contact_name

            Glide.with(itemView.context).load(items.contact_avatar).into(contact_avatar)
            itemView.setOnClickListener{
                listener(items)
            }
        }
    }


    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact_view, parent, false)
        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(ContactItemsFiltered[position], listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = ContactItemsFiltered.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    ContactItemsFiltered = ContactItems - selectedContactItems
                } else {
                    val filteredList: MutableList<ContactItem> = ArrayList()
                    val tempList = ContactItems - selectedContactItems
                    for (row in tempList) {
                        if (row.contact_name.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT))
                        ) {
                                filteredList.add(row)
                        }
                    }
                    ContactItemsFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = ContactItemsFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                ContactItemsFiltered = filterResults.values as ArrayList<ContactItem>
                notifyDataSetChanged()
            }
        }
    }

}