package com.google.firebase.samples.apps.mlkit.translate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.component_button_insert.*

class ActivityStart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        trActivity.setOnClickListener {
            val intent = Intent(this, TextRecognitionActivity::class.java)
            startActivity(intent)
        }
        savedActivity.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)
        }
        testActivity.setOnClickListener {
            val intent = Intent(this, test3::class.java)
            startActivity(intent)
        }
    }
}
