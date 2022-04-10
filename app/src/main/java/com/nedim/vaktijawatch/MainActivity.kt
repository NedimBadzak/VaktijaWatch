package com.nedim.vaktijawatch

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
    private var grad: Int = 0
    private lateinit var progressBar: ProgressBar
    lateinit var db: Database
    var lista: List<String?> = listOf()

    //    private lateinit var mDetector : GestureDetectorCompat
    //    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
        db = Database(applicationContext)
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
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            "postavke",
            Context.MODE_PRIVATE
        )
        grad = if (sharedPreferences.getInt("grad", 0) == 0) {
            77
        } else {
            sharedPreferences.getInt("grad", 0)
        }

        val localTime: Date = Calendar.getInstance().time
        progressBar.visibility = View.VISIBLE
        lista = Utils.getCorrectList(
            db.getPrayerTimesSec(
                SimpleDateFormat("M").format(localTime).toString().toInt(),
                SimpleDateFormat("d").format(localTime).toString().toInt(),
                grad+1
            ), SimpleDateFormat("YYYY").format(localTime).toString().toInt(),
            SimpleDateFormat("M").format(localTime).toString().toInt(),
            SimpleDateFormat("d").format(localTime).toString().toInt()
        )
        Log.d("TAGIC", db.getLocationName(grad+1))
        Log.v("TAGIC", "Ide bazom")
        for (i in lista.indices) {
            listaVakat.add(
                Vakat(
                    imenaVaktova[i],
                    SimpleDateFormat("HH:mm").parse(lista[i])
                )
            )
        }
        vakatRecycler.adapter = VakatListAdapter(
            listaVakat
        )
        vakatRecycler.requestFocus()
        progressBar.visibility = View.GONE


        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            val localTime: Date = Calendar.getInstance().time
            lista = Utils.getCorrectList(
                db.getPrayerTimesSec(
                    SimpleDateFormat("M").format(localTime).toString().toInt(),
                    SimpleDateFormat("d").format(localTime).toString().toInt(),
                    grad+1
                ), SimpleDateFormat("YYYY").format(localTime).toString().toInt(),
                SimpleDateFormat("M").format(localTime).toString().toInt(),
                SimpleDateFormat("d").format(localTime).toString().toInt()
            )
            Log.d("TAGIC", db.getLocationName(grad+1))
            Log.v("TAGIC", "Ide bazom")
            listaVakat.clear();
            for (i in lista.indices) {
                listaVakat.add(
                    Vakat(
                        imenaVaktova[i],
                        SimpleDateFormat("HH:mm").parse(lista[i])
                    )
                )
            }
            vakatRecycler.adapter = VakatListAdapter(
                listaVakat
            )
            pullToRefresh.isRefreshing = false
        }
    }


/*
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
*/

/*
    private fun isConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
*/



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