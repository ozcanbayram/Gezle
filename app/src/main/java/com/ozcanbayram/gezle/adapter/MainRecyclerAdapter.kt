package com.ozcanbayram.gezle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozcanbayram.gezle.databinding.RecyclerRowBinding
import com.ozcanbayram.gezle.model.Post
import com.squareup.picasso.Picasso

class MainRecyclerAdapter(private val postList : ArrayList<Post>) : RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerHolder>() {

    class MainRecyclerHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerHolder, position: Int) {
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.imageView4)
        holder.binding.textView7.text = postList.get(position).ad_soyad
        holder.binding.textView.text = postList.get(position).place_name
        holder.binding.textView8.text = postList.get(position).comment
        holder.binding.textView6.text = postList.get(position).email
    }

}