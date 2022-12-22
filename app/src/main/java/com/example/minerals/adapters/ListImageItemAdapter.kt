package com.example.minerals.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.minerals.data.Mineral
import com.example.minerals.R

class ListImageItemAdapter:
    RecyclerView.Adapter<ListImageItemAdapter.ViewHolder>() {
    var onItemClick: ((Mineral) -> Unit)? = null
    var onItemLongClick: ((Mineral) -> Unit)? = null

    private var mineralsList: List<Mineral> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val firstLineTextView: TextView
        val secondLineTextView: TextView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.image)
            firstLineTextView = itemView.findViewById<TextView>(R.id.firstLine)
            secondLineTextView = itemView.findViewById<TextView>(R.id.secondLine)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val mineral: Mineral = mineralsList.get(position)
        viewHolder.imageView.setImageURI(Uri.parse(mineral.image))
        viewHolder.firstLineTextView.setText(mineral.note)
        viewHolder.secondLineTextView.setText(mineral.type)
        viewHolder.itemView.setOnClickListener {
            onItemClick?.invoke(mineralsList[position])
        }

        viewHolder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(mineralsList[position])
            true
        }
    }

    override fun getItemCount(): Int { return mineralsList.count() }

    fun setData(minerals: List<Mineral>) {
        this.mineralsList = minerals
        notifyDataSetChanged()
    }
}