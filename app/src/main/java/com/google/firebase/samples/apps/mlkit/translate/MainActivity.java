package com.google.firebase.samples.apps.mlkit.translate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.sentImage);
        // Get the intent that started this activity
        Intent intent = getIntent();
        // Get the action of the intent
        String action = intent.getAction();
        // Get the type of intent (Text or Image)
        String type = intent.getType();
        // When Intent's action is 'ACTION+SEND' and Tyoe is not null
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // When tyoe is 'text/plain'
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) { // When type is 'image/*'
                handleSendImage(intent); // Handle single image being sent
            }
        }

    }

    /**
     * Method to hanlde incoming text data
     * @param intent
     */
    private void handleSendText(Intent intent) {
        // Get the text from intent
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        // When Text is not null
        if (sharedText != null) {
            // Show the text as Toast message
            Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to handle incoming Image
     * @param intent
     */
    private void handleSendImage(Intent intent) {
        // Get the image URI from intent
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        // When image URI is not null
        if (imageUri != null) {
            // Update UI to reflect image being shared
            imageView.setImageURI(imageUri);
        } else{
            Toast.makeText(this, "Error occured, URI is invalid", Toast.LENGTH_LONG).show();
        }
    }
}