package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.*

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var textspan: EditText? = null
    var tags: FlexboxLayout? = null
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
    @SuppressLint("SetTextI18n")
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
            val completeString =blockText
            val splited = completeString.split(" ").toTypedArray()
            var list=Arrays.asList(*splited)

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


}