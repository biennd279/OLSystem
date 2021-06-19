package org.teamseven.ols.ui.conversation.new_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R
import org.teamseven.ols.databinding.ItemContactViewBinding
import org.teamseven.ols.databinding.ItemSelectedContactBinding
import org.teamseven.ols.entities.User

class SelectedContactAdapter(
    private var selectedContactItems: List<User>,
    private val listener: (User) -> Unit
) : ListAdapter<User, RecyclerView.ViewHolder>(UserCallback()) {

    class ViewHolder private constructor(
        private val binding: ItemSelectedContactBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val username = binding.textSelectedContactName

        fun bind(item: User, listener: (User) -> Unit) {
            username.text = item.name

            itemView.setOnClickListener{
                listener(item)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemSelectedContactBinding.inflate(
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
    override fun getItemCount() = selectedContactItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(selectedContactItems[position], listener)
            }
        }
    }


}
