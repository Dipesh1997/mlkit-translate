package com.google.firebase.samples.apps.mlkit.translate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {
    private var sourceText: String? = null
    private var SpeakWord: TextToSpeech?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        val value = intent.getStringExtra("SINGLETEXT")!!
        singleText.append(value + "\n")

        //Speak Initialize
        SpeakWord= TextToSpeech(this, TextToSpeech.OnInitListener {})

        //Translate button
        translateButton.setOnClickListener {
            sourceText = singleText!!.text.toString()
            val options = FirebaseTranslatorOptions.Builder() //from language
                .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
                .setTargetLanguage(FirebaseTranslateLanguage.HI)
                .build()
            val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
            val conditions = FirebaseModelDownloadConditions.Builder().build()
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
                translator.translate(sourceText!!).addOnSuccessListener { s -> singleText!!.text = s }
            }
        }

        //Speak Button
        speakButton.setOnClickListener {
            val toSpeak = singleText!!.text.toString()
            SpeakWord!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        }

    }
}
