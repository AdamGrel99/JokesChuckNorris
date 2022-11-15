package com.example.jokesappwithchucknorris

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun chuckNorrisClick(view: View){
        Ion.with(this)
            .load("http://api.icndb.com/jokes/random")
            .asString()
            .setCallback{ _, result ->
                Log.d("Marty","The Json data is:\n $result")
                processChuckData(result)
            }
    }

    /*
    This function pulls data from the Chuck Norris API.
    The data is in the following format:
        {
            "type": "success",
            "value": {
                "id": 552,
                "joke": "Chuck Norris knows the value of NULL, and he can sort by it too.",
                "categories": ["nerdy"]
                }
         }
     */

    private fun processChuckData(result: String) {
        val json = JSONObject(result)
        val obj = json.getJSONObject("value")
        val joke = obj.getString("joke")
        output.text = joke
    }

    fun catClick(view: View){
        Ion.with(this)
            .load("http://thecatapi.com/api/images/get?format=json&size=med&results_per_page=8")
            .asString()
            .setCallback{ _, result ->
                Log.d("Marty","The Json data is:\n $result")
                processCatData(result)
            }
    }

    private fun processCatData(result: String) {
        val array = JSONObject("{\"images\":$result}")
            .getJSONArray("images")

        grid.removeAllViews()
        for (i in 0 until array.length()) {
            val url = array.getJSONObject(i).getString("url")
            val img = ImageView(this)
            grid.addView(img)
            img.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            Picasso.get()
                .load(url)
                .into(img)
        }
    }
}