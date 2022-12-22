package com.example.minerals.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.minerals.Mineral
import com.example.minerals.R

class ListImageItemAdapter internal constructor(private val dataSource: ArrayList<Mineral>) :
    RecyclerView.Adapter<ListImageItemAdapter.ViewHolder>() {
    var onItemClick: ((Mineral) -> Unit)? = null
    var onItemLongClick: ((Mineral) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val firstLineTextView = itemView.findViewById<TextView>(R.id.firstLine)
        val secondLineTextView = itemView.findViewById<TextView>(R.id.secondLine)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataSource[adapterPosition])
            }

            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(dataSource[adapterPosition])
                true
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val Mineral: Mineral = dataSource.get(position)
        val imageView = viewHolder.imageView
        imageView.setImageURI(Mineral.image)
        val firstLineTextView = viewHolder.firstLineTextView
        firstLineTextView.setText(Mineral.name)
        val secondLineTextView = viewHolder.secondLineTextView
        secondLineTextView.setText(Mineral.type)
    }

    override fun getItemCount(): Int { return dataSource.size }
}