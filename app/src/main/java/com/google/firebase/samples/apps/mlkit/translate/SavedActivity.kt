package com.google.firebase.samples.apps.mlkit.translate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_saved.*


class SavedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        val dataSaveHelper = DataSaveHelper(this)
        Log.d("read","read saved data")
        val noteslist = dataSaveHelper.getAllNotes()
        val notesAdapter = NotesAdapter(this@SavedActivity,noteslist)
        rv_notes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SavedActivity)
            adapter = notesAdapter
        }
        notesAdapter.getUpdate()

    }

}