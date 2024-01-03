package com.ozcanbayram.gezle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozcanbayram.gezle.databinding.RecyclerRowForProfileBinding
import com.ozcanbayram.gezle.model.Post
import com.ozcanbayram.gezle.model.ProfilePost
import com.squareup.picasso.Picasso

class ProfileRecyclerAdapter(private var profilePostArrayList : ArrayList<ProfilePost>) : RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(val binding : RecyclerRowForProfileBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = RecyclerRowForProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return profilePostArrayList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        Picasso.get().load(profilePostArrayList.get(position).downloadUrl).into(holder.binding.photo)
        holder.binding.name.text = profilePostArrayList.get(position).ad_soyad
        holder.binding.place.text = profilePostArrayList.get(position).place_name
        holder.binding.comment.text = profilePostArrayList.get(position).comment
        holder.binding.email.text = profilePostArrayList.get(position).email
    }

}