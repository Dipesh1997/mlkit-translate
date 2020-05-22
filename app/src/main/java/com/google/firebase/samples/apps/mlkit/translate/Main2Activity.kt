package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import java.util.*

class Main2Activity : AppCompatActivity() {
    var tags: FlexboxLayout? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        tags = findViewById(R.id.tagsView)
        val completeString =
            "New string is very very big lets check flexbox solves my problem or not if not it is not useful to me"
        val splited = completeString.split(" ").toTypedArray()
        val list=Arrays.asList(*splited)

        for (i in list.indices) {
            val textView = TextView(this)
            val partToClick = list[i]
            with(tags) {
                textView.text = "$partToClick "
                textView.setOnClickListener { v: View? ->
                        Toast.makeText(
                            applicationContext,
                            partToClick,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                this?.addView(textView)
            }
        }
    }
}