package com.google.firebase.samples.apps.mlkit.translate;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Expand extends AppCompatActivity {

    RecyclerView recyclerView;

    List<WordMeaning> wordMeaningList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);

        recyclerView = findViewById(R.id.recyclerView);
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        MeaningAdapter meaningAdapter = new MeaningAdapter(wordMeaningList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(meaningAdapter);
    }

    private void initData() {
        String value = getIntent().getStringExtra("RECTEXT");
        assert value != null;
        String[] list =value.split(" ");
        final int arraySize = list.length;
        wordMeaningList = new ArrayList<>();

        for(int i = 0; i < arraySize; i++) {
            final String singleElement = list[i];
            String partToClick = singleElement;
            wordMeaningList.add(new WordMeaning(partToClick, "5", "200","Touch to get Meaning"));

        }

    }


}