package org.teamseven.ols.ui.classes.tabs.file

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
        val file_type_icon = view.findViewById<ImageView>(R.id.img_file_type_icon)
        val file_name = view.findViewById<TextView>(R.id.text_file_name)
        val file_upload_date = view.findViewById<TextView>(R.id.text_file_upload_date)

        fun bindItem(items: FileItem, listener: (FileItem) -> Unit) {
            file_name.text = items.file_name
            file_upload_date.text = items.upload_date

            Glide.with(itemView.context).load(items.file_type_icon).into(file_type_icon)

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


    override fun getItemCount(): Int = filesItems.size


}