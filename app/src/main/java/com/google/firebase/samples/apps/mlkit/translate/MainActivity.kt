package com.google.firebase.samples.apps.mlkit.translate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<View>(R.id.sentImage) as ImageView
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
            Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Method to handle incoming Image
     * @param intent
     */
    private fun handleSendImage(intent: Intent) {
        // Get the image URI from intent
        val imageUri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri
        // When image URI is not null
        if (imageUri != null) {
            // Update UI to reflect image being shared
            imageView!!.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "Error occured, URI is invalid", Toast.LENGTH_LONG).show()
        }
    }
}