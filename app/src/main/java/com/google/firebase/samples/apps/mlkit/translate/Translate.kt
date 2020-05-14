package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

@SuppressLint("Registered")
class Translate : AppCompatActivity() {
    private var mSourcetext: EditText? = null
    private var mTranslateBtn: Button? = null
    private var mTranslatedText: TextView? = null
    private var sourceText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        mSourcetext = findViewById(R.id.sourceText)
        mTranslateBtn = findViewById(R.id.translate)
        mTranslatedText = findViewById(R.id.translatedText)
        mTranslateBtn?.run { this.setOnClickListener { translate() } }
    }

    private fun translate() {
        sourceText = mSourcetext!!.text.toString()
        val options = FirebaseTranslatorOptions.Builder() //from language
            .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
            .setTargetLanguage(FirebaseTranslateLanguage.HI)
            .build()
        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        val conditions = FirebaseModelDownloadConditions.Builder().build()
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            translator.translate(sourceText!!).addOnSuccessListener { s -> mTranslatedText!!.text = s }
        }
    }
}