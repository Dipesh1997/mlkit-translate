package com.google.firebase.samples.apps.mlkit.translate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_save.*
import kotlinx.android.synthetic.main.component_button_insert.*


class SaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        trActivity.setOnClickListener{
            val intent = Intent(this,InsertActivity::class.java)
            startActivity(intent)
        }

        val dataHelper = DataHelper(this)
        Log.d("read","read semua data")
        val studentlist = dataHelper.getAllStudent()
        val studentAdapter = StudentAdapter(this@SaveActivity,studentlist)
        rv_student.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SaveActivity)
            adapter = studentAdapter
        }
        studentAdapter.getUpdate()

    }

}