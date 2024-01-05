package com.example.rickandmorty10

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class RickRecycler : RecyclerView.Adapter<RickRecycler.ViewHolder>() {

    // List of characters to display
    var list = ArrayList<Result>()

    // Set the data for the adapter and
    // notify the RecyclerView of the change
    fun setData(list: List<Result>) {
        this.list = list as ArrayList<Result>
        notifyDataSetChanged()
    }

    // Initialise single item using view holder class
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val characterImage = view.findViewById<CircleImageView>(R.id.characterImage)  // Assuming CircleImageView
        val name = view.findViewById<TextView>(R.id.showName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = this.list  // Access the list from the adapter
        // Loading image using glide library
        Glide.with(holder.itemView).load(list[position].image).into(holder.characterImage)

        // Loading character name in text View
        holder.name.text = list[position].name

        holder.itemView.setOnClickListener {
            val selectedCharacter = list[position]
            val intent = Intent(holder.itemView.context, CharacterDetailsActivity::class.java)
            intent.putExtra("character", selectedCharacter as Parcelable)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}