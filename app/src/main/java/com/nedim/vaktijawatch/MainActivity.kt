package com.nedim.vaktijawatch

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.view.BoxInsetLayout
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : WearableActivity() {
    private var grad : Int = 0
    private lateinit var progressBar : ProgressBar
//    private lateinit var mDetector : GestureDetectorCompat
    //    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
//        mDetector = GestureDetectorCompat(this, MyGestureListener())
        var vakatRecycler: RecyclerView = findViewById(R.id.vakatRecycler)
        progressBar = findViewById(R.id.progressBar)
//        progressBar.visibility = View.GONE

        vakatRecycler.layoutManager = GridLayoutManager(this, 1)
        var listaVakat: MutableList<Vakat> = mutableListOf()
        val imenaVaktova: List<String> = listOf(
            "Zora",
            "Izlazak sunca",
            "Podne",
            "Ikindija",
            "Akšam",
            "Jacija"
        )
        var content: String? = null
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("postavke",
            Context.MODE_PRIVATE)
        grad = if(sharedPreferences.getInt("grad", 0) == 0) {
            77
        } else {
            sharedPreferences.getInt("grad", 0)
        }

        val thread = Thread {
            val localTime: Date = Calendar.getInstance().time
            progressBar.visibility = View.VISIBLE
            content = dajContentStranice(
                SimpleDateFormat("dd").format(localTime).toString().toInt(),
                SimpleDateFormat("MM").format(localTime).toString().toInt(),
                SimpleDateFormat("YYYY").format(localTime).toString().toInt()
            )
            runOnUiThread {
                if (content != null) {
                    var jsonObject: JSONObject = JSONObject(content)
                    var jsonArray: JSONArray = jsonObject.getJSONArray("vakat")
                    for (i in 0 until jsonArray.length()) {
                        listaVakat.add(
                            Vakat(
                                imenaVaktova[i],
                                SimpleDateFormat("HH:mm").parse(jsonArray[i] as String)
                            )
                        )
                    }
                    vakatRecycler.adapter = VakatListAdapter(
                        listaVakat
                    )
                }
                progressBar.visibility = View.GONE
            }
        }
    thread.start()

    val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            Thread {
                val localTime: Date = Calendar.getInstance().time
                content = dajContentStranice(
                    SimpleDateFormat("dd").format(localTime).toString().toInt(),
                    SimpleDateFormat("MM").format(localTime).toString().toInt(),
                    SimpleDateFormat("YYYY").format(localTime).toString().toInt()
                )
                runOnUiThread {
                    if (content != null) {
                        var jsonObject: JSONObject = JSONObject(content)
                        var jsonArray: JSONArray = jsonObject.getJSONArray("vakat")
                        var novaLista: MutableList<Vakat> = mutableListOf()
                        for (i in 0 until jsonArray.length()) {
                            novaLista.add(
                                Vakat(
                                    imenaVaktova[i],
                                    SimpleDateFormat("HH:mm").parse(jsonArray[i] as String)
                                )
                            )
                        }
                        listaVakat = novaLista
                        vakatRecycler.adapter = VakatListAdapter(
                            listaVakat
                        )
                    }
                }
                pullToRefresh.isRefreshing = false
            }.start()
        }
    }

    private fun dajContentStranice(dan: Int, mjesec: Int, godina: Int): String? {
        var content: String? = null
        var connection: URLConnection? = null
        try {
            connection =
                URL("https://api.vaktija.ba/vaktija/v1/$grad/$godina/$mjesec/$dan").openConnection()
            val scanner = Scanner(connection.getInputStream())
            scanner.useDelimiter("\\Z")
            content = scanner.next()
            scanner.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return content
//        var siteContent : String = URL("http://www.google.com").readText()
//        return siteContent
    }



}

// Create the LocationRequest object
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                val gcd = Geocoder(baseContext, Locale.getDefault())
//                var addresses: List<Address> = listOf()
//                try {
//                    if (location != null) {
//                        addresses = gcd.getFromLocation(
//                            location.latitude,
//                            location.longitude, 1
//                        )
//                    }
//                    if (addresses.isNotEmpty()) {
//                        Log.v("TAGIC", cityName)
//                        cityName = addresses[0].locality
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }.addOnFailureListener {
//                Log.v("TAGIC", it.toString())
//            }
//        obtieneLocalizacion()