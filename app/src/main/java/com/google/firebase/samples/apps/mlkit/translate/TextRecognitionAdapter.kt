package com.google.firebase.samples.apps.mlkit.translate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TextRecognitionAdapter(private val context: Context, private val textRecognitionModels: List<TextRecognitionModel>) : RecyclerView.Adapter<TextRecognitionAdapter.TextRecognitionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextRecognitionViewHolder {
        return TextRecognitionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text_recognition, parent, false))
    }

    override fun onBindViewHolder(holder: TextRecognitionViewHolder, position: Int) {
        holder.text1.text = textRecognitionModels[position].id.toString()
        holder.text2.text = textRecognitionModels[position].text
        holder.itemView.setOnClickListener { v: View? ->
            Toast.makeText(context,holder.text2.text,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = textRecognitionModels.size

    class TextRecognitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_text_recognition_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_text_recognition_text_view2)!!
    }
}