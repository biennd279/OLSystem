package org.teamseven.ols.ui.classes.tabs.people

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R
import org.teamseven.ols.databinding.ItemMemberViewBinding

class PeopleAdapter(
    private val context: Context,
    private var peopleItems: List<PeopleItem>,
    private val listener: (PeopleItem) -> Unit
) : ListAdapter<PeopleItem, RecyclerView.ViewHolder>(PeopleCallBack()) {


    //ViewHolder
    class ViewHolder private constructor(
        private val binding: ItemMemberViewBinding
        ): RecyclerView.ViewHolder(binding.root) {

        private val username = binding.textMemberName
        private val avatar = binding.imgMemberAvatar

        fun bind(item: PeopleItem, listener: (PeopleItem) -> Unit) {
            username.text = item.username

            Glide.with(itemView.context).load(item.avatar).into(avatar)
            itemView.setOnClickListener{
                listener(item)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemMemberViewBinding.inflate(
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


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(peopleItems[position], listener)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = peopleItems.size
}

class PeopleCallBack(): DiffUtil.ItemCallback<PeopleItem>() {
    override fun areItemsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
        return oldItem == newItem
    }

}