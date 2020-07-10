package com.google.firebase.samples.apps.mlkit.translate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*

class Test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var min = 0
        var max: Int = my_text_view.getText().length
        if (my_text_view.isFocused()) {
            val selStart: Int = my_text_view.getSelectionStart()
            val selEnd: Int = my_text_view.getSelectionEnd()
            min = Math.max(0, Math.min(selStart, selEnd))
            max = Math.max(0, Math.max(selStart, selEnd))
        }

        // here is your selected text
        val selectedText: CharSequence = my_text_view.getText().subSequence(min, max)
        val text = selectedText.toString()
        val clip = ClipData.newPlainText("Copied Text",text)
        clipboard.setPrimaryClip(clip)
        my_text_view.setOnClickListener {
            Toast.makeText(applicationContext,clip.toString(), Toast.LENGTH_SHORT).show()
        }


        /*my_text_view.makeLinks(
            Pair("Terms of Service", View.OnClickListener {
                Toast.makeText(applicationContext, "Terms of Service Clicked", Toast.LENGTH_SHORT).show()
            }),
            Pair("Privacy", View.OnClickListener {
                Toast.makeText(applicationContext, "Privacy Clicked", Toast.LENGTH_SHORT).show()
            }),
            Pair("Policy",View.OnClickListener {
                Toast.makeText(applicationContext, "Policy Clicked", Toast.LENGTH_SHORT).show()
            })
        )*/
    }
    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)

                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

}
