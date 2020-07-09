package com.google.firebase.samples.apps.mlkit.translate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notes.view.*


class NotesAdapter (private val context: Context, private var notesList: ArrayList<Notes>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesAdapter.ViewHolder, position: Int) {
        holder.onBind(notesList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun onBind(notes: Notes){
            itemView.tv_time.text = notes.time.toString()
            itemView.tv_name.text = notes.name
            itemView.setOnClickListener {
                context.startActivity(
                    Intent(context, ViewActivity::class.java).putExtra(
                        "SINGLETEXT",
                        notes.name
                    )
                )
            }
            itemView.setOnLongClickListener{
                val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                alertDialogBuilder.setTitle("Confirm")
                    .setMessage("Are you sure to delete "+notes.name+"?")
                    .setCancelable(true)
                    .setPositiveButton("No"){dialog,which->
                        Toast.makeText(itemView.context, "cancel delete ", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Yes"){dialog,which->
                        val db = DataSaveHelper(itemView.context)
                        db.deleteNotes(notes)
                        notesList.remove(notes)
                        notifyDataSetChanged()
                        Toast.makeText(itemView.context,"delete success",Toast.LENGTH_SHORT).show()
                    }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                true
            }
        }
    }

    fun getUpdate(){
        val db = DataSaveHelper(context)
        notesList = db.getAllNotes()
        notifyDataSetChanged()
    }
}