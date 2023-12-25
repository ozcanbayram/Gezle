package com.ozcanbayram.gezle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozcanbayram.gezle.databinding.RecyclerRowBinding
import com.ozcanbayram.gezle.model.Post

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
        holder.binding.recyclerViewRowView
        holder.binding.textView7.text = postList.get(position).ad_soyad
        holder.binding.textView.text = postList.get(position).place_name
        holder.binding.textView8.text = postList.get(position).comment
    }

}