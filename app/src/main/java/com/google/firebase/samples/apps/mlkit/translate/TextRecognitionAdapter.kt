package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_edittext.view.*

class TextRecognitionAdapter(private val context: Context, private val textRecognitionModels: List<TextRecognitionModel>) : RecyclerView.Adapter<TextRecognitionAdapter.TextRecognitionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextRecognitionViewHolder {
        return TextRecognitionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_text_recognition, parent, false))
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: TextRecognitionViewHolder, position: Int) {
        holder.text1.text = textRecognitionModels[position].id.toString()
        holder.text2.text = textRecognitionModels[position].text
        holder.itemView.setOnClickListener {
            Toast.makeText(context, holder.text2.text, Toast.LENGTH_SHORT).show()

            context.startActivity(
                Intent(context, Expand::class.java).putExtra(
                    "RECTEXT",
                    holder.text2.text.toString()
                )
            )
        }

        holder.itemView.setOnLongClickListener{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.custom_edittext, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("Change Value")
            val  mAlertDialog = mBuilder.show()
            mDialogView.dialogChangeEt.setText(holder.text2.text)
            mDialogView.dialogChangeBtn.setOnClickListener {
                mAlertDialog.dismiss()
                val changedValue = mDialogView.dialogChangeEt.text.toString()
                Toast.makeText(context,changedValue,Toast.LENGTH_SHORT).show()
                holder.text2.text = changedValue
            }
            mDialogView.dialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
            true
        }
    }



    override fun getItemCount() = textRecognitionModels.size

    class TextRecognitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1 = itemView.findViewById<TextView>(R.id.item_text_recognition_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_text_recognition_text_view2)!!
    }
}