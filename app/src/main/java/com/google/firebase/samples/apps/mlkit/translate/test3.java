package com.google.firebase.samples.apps.mlkit.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

@SuppressWarnings("ALL")
public class test3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        EditText editText = (EditText) findViewById(R.id.editTest);
        String stringYouExtracted = editText.getText().toString();
        int startIndex = editText.getSelectionStart();
        int endIndex = editText.getSelectionEnd();
        stringYouExtracted = (String) stringYouExtracted.subSequence(startIndex, endIndex);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        assert clipboard != null;
        clipboard.setText(stringYouExtracted);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            //android.text.ClipboardManager clipboard2 = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(stringYouExtracted);
        } else {
           // android.content.ClipboardManager clipboard3 = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", stringYouExtracted);
            clipboard.setPrimaryClip(clip);
        }
/*        int min = 0;
        int max = editText.getText().length();
        if (editText.isFocused()) {
            final int selStart = editText.getSelectionStart();
            final int selEnd = editText.getSelectionEnd();
            min = Math.max(0, Math.min(selStart, selEnd));
            max = Math.max(0, Math.max(selStart, selEnd));
        }
// here is your selected text
        final CharSequence selectedText = editText.getText().subSequence(min, max);
        String text = selectedText.toString();


// copy to clipboard
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);*/

    }
}