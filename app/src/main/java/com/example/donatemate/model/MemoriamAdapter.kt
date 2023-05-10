package com.example.donatemate.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donatemate.R
import com.google.firebase.database.*

class MedicineAdapter(
    private val context: Context,
    private val list: ArrayList<MemoriamData>,
    private val onDelete: (MemoriamData) -> Unit
) : RecyclerView.Adapter<MedicineAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hName: TextView = itemView.findViewById(R.id.txt_hName)
        var hRelation: TextView = itemView.findViewById(R.id.txt_hRelation)
        var hBio: TextView = itemView.findViewById(R.id.txt_hBio)
        var hDeleteButton: Button = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.summary_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val memoriam = list[position]
        holder.hName.text = memoriam.honourName
        holder.hRelation.text = memoriam.honourRela
        holder.hBio.text = memoriam.honourBio

        holder.hDeleteButton.setOnClickListener {
            // Remove the item from the list
            list.removeAt(position)

            // Call the onDelete function to remove the item from the database
            onDelete(memoriam)

            // Notify the adapter that the item has been removed
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}