@file:Suppress("DEPRECATION")

package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.custom_edittext.view.*
import kotlinx.android.synthetic.main.custom_edittext.view.dialogCancelBtn
import kotlinx.android.synthetic.main.custom_edittext.view.dialogChangeBtn
import kotlinx.android.synthetic.main.custom_edittext.view.dialogChangeEt
import kotlinx.android.synthetic.main.dialog_meaning.*
import kotlinx.android.synthetic.main.dialog_meaning.view.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var textspan: EditText? = null
    var tags: FlexboxLayout? = null
    var url: String? = null
    var wordfromEdit:String?=null
    private var sourceText: String? = null
    private var SpeakWord:TextToSpeech?=null
    private var SpeakMeaning:TextToSpeech?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<View>(R.id.sentImage) as ImageView
        textspan = findViewById<View>(R.id.textspan) as EditText
        tags = findViewById(R.id.tagsView)
        // Get the intent that started this activity
        val intent = intent
        // Get the action of the intent
        val action = intent.action
        // Get the type of intent (Text or Image)
        val type = intent.type
        // When Intent's action is 'ACTION+SEND' and Tyoe is not null

        SpeakWord= TextToSpeech(this, TextToSpeech.OnInitListener {

        })
        SpeakMeaning= TextToSpeech(this, TextToSpeech.OnInitListener {  })
        if (Intent.ACTION_SEND == action && type != null) {
            // When tyoe is 'text/plain'
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            } else if (type.startsWith("image/")) { // When type is 'image/*'
                handleSendImage(intent) // Handle single image being sent
            }
        }
    }

    /**
     * Method to hanlde incoming text data
     * @param intent
     */
    private fun handleSendText(intent: Intent) {
        // Get the text from intent
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        // When Text is not null
        if (sharedText != null) {
            // Show the text as Toast message
            textspan?.setText(sharedText)
            Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Method to handle incoming Image
     * @param intent
     */
    private fun handleSendImage(intent: Intent)
    {
        // Get the image URI from intent
        val imageUri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri
        // When image URI is not null
        imageView!!.setImageURI(imageUri)
        val bitmap = (imageView!!.drawable as BitmapDrawable).bitmap
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                processResultText(firebaseVisionText)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to detect",Toast.LENGTH_SHORT).show()
            }
    }
    @SuppressLint("SetTextI18n", "InflateParams")
    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            Toast.makeText(this," No text found",Toast.LENGTH_SHORT).show()
            return
        }
        for (block in resultText.textBlocks) {

            val blockText = block.text
            Toast.makeText(this,blockText,Toast.LENGTH_SHORT).show()
            textspan?.append(blockText +"\n")
            //editText.append(blockText + " ")
            //editText.append(blockText + "\n")
            for(line in block.lines){
                val completeString =line.text
                val splited = completeString.split(" ").toTypedArray()
                val list=Arrays.asList(*splited)

                for (i in list.indices) {
                    var textView = TextView(this)
                    var partToClick = list[i]
                    with(tags) {
                        textView.text = "$partToClick "
                        textView.setOnClickListener { v: View? ->
                            Toast.makeText(
                                applicationContext,
                                partToClick,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        textView.setOnLongClickListener{
                            val mDialogView = LayoutInflater.from(this?.context).inflate(R.layout.dialog_meaning, null)
                            val mBuilder = AlertDialog.Builder(this?.context).setView(mDialogView).setTitle("Change Value")
                            val  mAlertDialog = mBuilder.show()
                            //Setting Dialog Initial Value
                            mDialogView.dialogChangeEt.setText(partToClick)
                            val word = partToClick
                            val noSpaceStr = word.replace("\\s".toRegex(), "")
                            wordfromEdit=noSpaceStr
                            //Oxford Meaning
                            val dictionaryRequest = DictionaryRequest(mDialogView.meaning)
                            url = dictionaryEntries()
                            dictionaryRequest.execute(url)
                            //Translate to hindi word
                            sourceText = wordfromEdit
                            val options = FirebaseTranslatorOptions.Builder() //from language
                                .setSourceLanguage(FirebaseTranslateLanguage.EN) // to language
                                .setTargetLanguage(FirebaseTranslateLanguage.HI)
                                .build()
                            val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
                            val conditions = FirebaseModelDownloadConditions.Builder().build()
                            translator.downloadModelIfNeeded(conditions)
                                .addOnSuccessListener {
                                    translator.translate(sourceText!!)
                                        .addOnSuccessListener { s -> mDialogView.hindi.text = s }
                                }
                            //Click on Hindi Word to change meaning to hindi
                            mDialogView.hindi.setOnClickListener {

                                sourceText = mDialogView.meaning.text as String?
                                translator.downloadModelIfNeeded(conditions)
                                    .addOnSuccessListener {
                                        translator.translate(sourceText!!)
                                            .addOnSuccessListener { s -> mDialogView.meaning.text = s }
                                    }

                            }
                            //Click on Speak button
                            mDialogView.speakSet.setOnClickListener {
                                val toSpeak = wordfromEdit
                                SpeakWord!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
                                val toSpeakMeaning = mDialogView.meaning.text.toString()
                                SpeakMeaning!!.speak(toSpeakMeaning, TextToSpeech.QUEUE_FLUSH, null)
                                Toast.makeText(this?.context,"Speaking",Toast.LENGTH_SHORT).show()

                            }
                            //Click on Change Button
                            mDialogView.dialogChangeBtn.setOnClickListener {
                                mAlertDialog.dismiss()
                                val changedValue = mDialogView.dialogChangeEt.text.toString()
                                Toast.makeText(this?.context,changedValue,Toast.LENGTH_SHORT).show()
                                partToClick = changedValue
                                textView.text="$changedValue "
                            }
                            //Click on Cancel Button
                            mDialogView.dialogCancelBtn.setOnClickListener {
                                mAlertDialog.dismiss()
                            }
                            true
                        }

                        this?.addView(textView)
                    }
                }

            }

        }

    }

    private fun dictionaryEntries(): String {
        val language = "en-gb"

        val word = wordfromEdit
        val noSpaceStr = word?.replace("\\s".toRegex(), "")
        val fields = "definitions"
        val strictMatch = "false"
        val wordId = noSpaceStr?.toLowerCase(Locale.ROOT)
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/$language/$wordId?fields=$fields&strictMatch=$strictMatch"
    }
}