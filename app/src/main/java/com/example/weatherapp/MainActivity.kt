package com.example.weatherapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.parseColor("#1383C3")

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        getJsonData(lat,long)
    }

    private fun getJsonData(lat : String? , long : String?) {


        val api_key = "e5ad7a5b8a286655cd6b5ff858a520ef"

    val queue = Volley.newRequestQueue(this)
    val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${api_key}"


    val jsonRequest = JsonObjectRequest(
        Request.Method.GET, url,null,
            Response.Listener { response ->

                setValues(response)
            },
            Response.ErrorListener {Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show() })

    queue.add(jsonRequest)


    }

    private fun setValues(response : JSONObject?) {

        if (response != null) {
            city.text = response.getString("name")
            var lat = response.getJSONObject("coord").getString("lat")
            var long = response.getJSONObject("coord").getString("lon")
            coordinates.text = "${lat},${long}"
            weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
            var tempr = response.getJSONObject("main").getString("temp")
            tempr = ((((tempr).toFloat() - 273.15)).toInt()).toString()

            temp.text = "${tempr}°C"

            var mintemp=response.getJSONObject("main").getString("temp_min")
             mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        min_temp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        max_temp.text=maxtemp+"°C"

        pressure.text=response.getJSONObject("main").getString("pressure")
        humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        wind.text=response.getJSONObject("wind").getString("speed")
        degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"


        }


    }
}