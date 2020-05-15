package com.google.firebase.samples.apps.mlkit.translate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Expand : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var wordMeaningList: MutableList<WordMeaning>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand)
        recyclerView = findViewById(R.id.recyclerView)
        initData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val meaningAdapter = MeaningAdapter(wordMeaningList!!)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = meaningAdapter
    }

    private fun initData() {
        val value = intent.getStringExtra("RECTEXT")!!
        val list = value.split(" ").toTypedArray()
        wordMeaningList = ArrayList()
        for (singleElement in list) {
            (wordMeaningList as ArrayList<WordMeaning>).add(WordMeaning(singleElement, "On", "Hindi Word", "Touch here to get Meaning"))
        }
    }
}