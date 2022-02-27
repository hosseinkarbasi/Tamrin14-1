package com.example.flickerpagination

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerpagination.data.FlickerResult
import com.example.flickerpagination.data.Photo
import com.example.flickerpagination.data.Photos
import com.squareup.picasso.Picasso

class RecyclerAdapter() : RecyclerView.Adapter<RecyclerAdapter.UsersViewHolder>() {

    private var list = ArrayList<Photo>()

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Photo) {
            with(itemView) {
                var tvTitle: TextView = findViewById(R.id.title)
                var imgView: ImageView = findViewById(R.id.Img01)
                tvTitle.text = data.title

                Picasso.with(imgView.context)
                    .load(data.url_s)
                    .into(imgView)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_image, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(items: List<Photo>?) {
        if (items != null) {
            list.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}
