package com.ozcanbayram.gezle.adapter

import ProfilePost
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ozcanbayram.gezle.databinding.RecyclerRowForProfileBinding
import com.ozcanbayram.gezle.model.Post
import com.ozcanbayram.gezle.view.MapsActivity
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

        holder.binding.showLocation.setOnClickListener {


            val intentForGoLocationsDetails = Intent(holder.itemView.context, MapsActivity::class.java)
            intentForGoLocationsDetails.putExtra("info","old")
            intentForGoLocationsDetails.putExtra("place", profilePostArrayList.get(position).place_name)
            intentForGoLocationsDetails.putExtra("lat", profilePostArrayList.get(position).latitudeInfo)
            intentForGoLocationsDetails.putExtra("long", profilePostArrayList.get(position).longitudeInfo)
            holder.itemView.context.startActivity(intentForGoLocationsDetails)
        }

        holder.binding.delete.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Uyarı")
                .setMessage("Paylaştığın Gez'i silmek üzeresin. Silmek istediğine emin misin?")
                .setPositiveButton("Evet", { dialog, which ->
                    // Evet düğmesine basıldığında yapılacak işlemler
                    val position = holder.adapterPosition
                    val postId = profilePostArrayList[position].id // Burada postId, silinecek belgenin Firestore'daki benzersiz kimliğini içermelidir.

                    // Firebase Firestore referansı oluştur
                    val db = FirebaseFirestore.getInstance()
                    val postsCollection = db.collection("Posts")

                    // Belgeyi sil
                    postsCollection.document(postId).delete()
                        .addOnSuccessListener {
                            // Silme başarılıysa, listeden de silebilirsiniz
                            profilePostArrayList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, profilePostArrayList.size)

                            Toast.makeText(holder.itemView.context, "Belge başarıyla silindi.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(holder.itemView.context, "Belge silinirken bir hata oluştu: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            Log.e("FirestoreError", "Belge silinirken hata oluştu", e)
                        }
                })
                .setNegativeButton("Hayır", { dialog, which ->
                    // Hayır düğmesine basıldığında yapılacak işlemler
                    dialog.dismiss()
                })

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()






        }

    }

}