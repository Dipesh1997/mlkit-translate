package com.google.firebase.samples.apps.mlkit.translate

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DictionaryRequest internal constructor(@field:SuppressLint("StaticFieldLeak") private val showDef: TextView) :
    AsyncTask<String?, Int?, String>() {
    public override fun doInBackground(vararg params: String?): String? {

        //TODO: replace with your own app id and app key
        val appid = "14db09b2"
        val appKey = "595c9c152bd69c5b93e7708a31fdc256"
        return try {
            val url = URL(params[0])
            val urlConnection =
                url.openConnection() as HttpsURLConnection
            urlConnection.setRequestProperty("Accept", "application/json")
            urlConnection.setRequestProperty("app_id", appid)
            urlConnection.setRequestProperty("app_key", appKey)

            // read the output from the server
            val reader =
                BufferedReader(InputStreamReader(urlConnection.inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            e.toString()
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        Log.d("msg", result)
        Log.v("Result Of Dictionary", "onPostExecute$result")
        val def: String
        try {
            val js = JSONObject(result)
            val results = js.getJSONArray("results")
            val lEntries = results.getJSONObject(0)
            val laArray = lEntries.getJSONArray("lexicalEntries")
            val entries = laArray.getJSONObject(0)
            val e = entries.getJSONArray("entries")
            val jsonObject = e.getJSONObject(0)
            val senseArray = jsonObject.getJSONArray("senses")
            val de = senseArray.getJSONObject(0)
            val d = de.getJSONArray("definitions")
            def = d.getString(0)
            showDef.text = def
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}