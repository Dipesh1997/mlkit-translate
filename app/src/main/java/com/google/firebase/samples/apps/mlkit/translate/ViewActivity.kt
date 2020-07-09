package com.google.firebase.samples.apps.mlkit.translate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        val value = intent.getStringExtra("SINGLETEXT")!!
        singleText.append(value+"\n")
    }
}
