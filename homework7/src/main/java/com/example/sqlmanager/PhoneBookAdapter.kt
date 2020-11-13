package com.example.sqlmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sqlmanager.sql.MyProfile
import kotlinx.android.synthetic.main.list_item.view.*

class PhoneBookAdapter(val data: List<MyProfile>): RecyclerView.Adapter<PhoneBookAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        private val phoneView = view.list_item_phone
        private val imageView = view.list_item_imageView
        private val nameView = view.list_item_name

        fun bind(data: MyProfile) {
            nameView.text = data.name
            phoneView.text = data.phoneNum
            Glide
                .with(view)
                .load(data.photoUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imageView)
        }
    }
}