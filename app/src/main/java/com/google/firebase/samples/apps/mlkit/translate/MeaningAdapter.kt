package com.google.firebase.samples.apps.mlkit.translate

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.samples.apps.mlkit.translate.MeaningAdapter.WordMeaningVH
import java.util.*

@Suppress("DEPRECATION")
class MeaningAdapter(var wordMeaningList: List<WordMeaning>) :
    RecyclerView.Adapter<WordMeaningVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordMeaningVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.meaning_row, parent, false)
        return WordMeaningVH(view)
    }

    override fun onBindViewHolder(holder: WordMeaningVH, position: Int) {
        val wordMeaning = wordMeaningList[position]
        holder.wordTextView.text = wordMeaning.word
        holder.hindiTextView.text = wordMeaning.hindi
        holder.speekTextView.text = wordMeaning.speak
        holder.meaningTextView.text = wordMeaning.wordmeaning
        val isExpanded = wordMeaningList[position].isExpanded
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return wordMeaningList.size
    }

    inner class WordMeaningVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var expandableLayout: ConstraintLayout = itemView.findViewById(R.id.expandableLayout)
        var wordTextView: TextView = itemView.findViewById(R.id.wordTextView)
        var hindiTextView: TextView = itemView.findViewById(R.id.hindiTextView)
        var speekTextView: TextView = itemView.findViewById(R.id.speekTextView)
        var meaningTextView: TextView = itemView.findViewById(R.id.meaningTextView)
        var url: String? = null
        private var sourceText: String? = null
        private var t1: TextToSpeech = TextToSpeech(itemView.context, OnInitListener {
            //if (status != TextToSpeech.ERROR) t1.language = Locale.US
        })
        private var t2: TextToSpeech = TextToSpeech(itemView.context, OnInitListener {
            //if (status != TextToSpeech.ERROR) t2.language = Locale.US
        })


        init {
            wordTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val wordMeaning = wordMeaningList[adapterPosition]
                    wordMeaning.isExpanded = !wordMeaning.isExpanded
                    notifyItemChanged(adapterPosition)
                    translate()
                }
            })
            hindiTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    translate()
                }

                private fun translate() {
                    sourceText = meaningTextView.text.toString()
                    val options = FirebaseTranslatorOptions.Builder() //from language
                        .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build()
                    val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
                    val conditions = FirebaseModelDownloadConditions.Builder().build()
                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            translator.translate(sourceText!!)
                                .addOnSuccessListener { s -> meaningTextView.text = s }
                        }
                }
            })
            speekTextView.setOnClickListener {
                val toSpeak = wordTextView.text.toString()
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
                val toSpeakMeaning = meaningTextView.text.toString()
                t2.speak(toSpeakMeaning, TextToSpeech.QUEUE_FLUSH, null)
            }
            meaningTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    toast()
                }

                private fun toast() {
                    val word = wordTextView.text.toString()
                    val noSpaceStr = word.replace("\\s".toRegex(), "")
                    Toast.makeText(itemView.context, noSpaceStr, Toast.LENGTH_SHORT).show()
                    val dictionaryRequest = DictionaryRequest(meaningTextView)
                    url = dictionaryEntries()
                    dictionaryRequest.execute(url)
                    translate()
                }

                private fun dictionaryEntries(): String {
                    val language = "en-gb"
                    val word = wordTextView.text.toString()
                    val noSpaceStr = word.replace("\\s".toRegex(), "")
                    val fields = "definitions"
                    val strictMatch = "false"
                    val wordId = noSpaceStr.toLowerCase(Locale.ROOT)
                    return "https://od-api.oxforddictionaries.com:443/api/v2/entries/$language/$wordId?fields=$fields&strictMatch=$strictMatch"
                }

                private fun translate() {
                    sourceText = wordTextView.text.toString()
                    val options = FirebaseTranslatorOptions.Builder() //from language
                        .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build()
                    val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
                    val conditions = FirebaseModelDownloadConditions.Builder().build()
                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            translator.translate(sourceText!!)
                                .addOnSuccessListener { s -> hindiTextView.text = s }
                        }
                }
            })
        }


        private fun translate() {
            sourceText = wordTextView.text.toString()
            val options = FirebaseTranslatorOptions.Builder() //from language
                .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
                .setTargetLanguage(FirebaseTranslateLanguage.HI)
                .build()
            val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
            val conditions = FirebaseModelDownloadConditions.Builder().build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(sourceText!!)
                        .addOnSuccessListener { s -> hindiTextView.text = s }
                }
        }
    }

    companion object

}