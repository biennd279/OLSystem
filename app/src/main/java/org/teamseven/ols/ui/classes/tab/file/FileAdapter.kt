package org.teamseven.ols.ui.classes.tab.file

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.teamseven.ols.R

class FileAdapter(
    private val context: Context,
    private val filesItems: List<FileItem>,
    private val listener: (FileItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {


    //ViewHolder
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name_of_file = view.findViewById<TextView>(R.id.name_of_file)
        val type_of_file = view.findViewById<ImageView>(R.id.type_of_file)
        val upload_date = view.findViewById<TextView>(R.id.upload_date)

        fun bindItem(items: FileItem, listener: (FileItem) -> Unit) {
            name_of_file.text = items.name_of_file
            upload_date.text = items.upload_date

            Glide.with(itemView.context).load(items.type_of_file).into(type_of_file)

            itemView.setOnClickListener{
                listener(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file_view, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(filesItems[position], listener)
    }


    override fun getItemCount(): Int =filesItems.size


}