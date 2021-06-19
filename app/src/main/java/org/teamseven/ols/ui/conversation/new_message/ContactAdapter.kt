package org.teamseven.ols.ui.conversation.new_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R
import org.teamseven.ols.databinding.ItemContactViewBinding
import org.teamseven.ols.databinding.ItemMemberViewBinding
import org.teamseven.ols.entities.User
import org.teamseven.ols.ui.classes.tabs.people.PeopleAdapter
import org.teamseven.ols.ui.classes.tabs.people.PeopleCallBack
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private val context: Context,
    private var contactItems: List<User>,
    private var selectedContactItems: List<User>,
    private val listener: (User) -> Unit
) : ListAdapter<User, RecyclerView.ViewHolder>(UserCallback()), Filterable {

    var contactItemsFiltered: List<User>
    init {
        contactItemsFiltered = contactItems - selectedContactItems
    }

    fun setContactItemsFiltered() {
        contactItemsFiltered = contactItems - selectedContactItems
    }

    class ViewHolder private constructor(
        private val binding: ItemContactViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val username = binding.textContactName
        private val avatar = binding.imgContactAvatar

        fun bind(item: User, listener: (User) -> Unit) {
            username.text = item.name

            if (item.avatarUrl != null) {
                Glide.with(itemView.context).load(item.avatarUrl).into(avatar)
            } else {
                Glide.with(itemView.context).load(R.drawable.ic_person_outline).into(avatar)
            }

            itemView.setOnClickListener{
                listener(item)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ContactAdapter.ViewHolder {
                val binding = ItemContactViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    // Create new views (invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = contactItemsFiltered.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    contactItemsFiltered = contactItems - selectedContactItems
                } else {
                    val filteredList: MutableList<User> = ArrayList()
                    val tempList = contactItems - selectedContactItems
                    for (row in tempList) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT))) {
                                filteredList.add(row)
                        }
                    }
                    contactItemsFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactItemsFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                contactItemsFiltered = filterResults.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(contactItems[position], listener)
            }
        }
    }

}

class UserCallback(): DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}
