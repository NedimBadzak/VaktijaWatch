package com.nedim.vaktijawatch

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class VakatListAdapter(
    var lista: List<Vakat>
) : RecyclerView.Adapter<VakatListAdapter.VakatListHolder>() {

    lateinit var sljedeciNamaz : Vakat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VakatListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.single_entry, parent, false)
        return VakatListHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: VakatListHolder, position: Int) {
        var localTime : Calendar = Calendar.getInstance()
        val simpleDateFormat : SimpleDateFormat = SimpleDateFormat("HH:mm")
        simpleDateFormat.timeZone = java.util.TimeZone.getDefault()
        holder.promijeniLokacijuButton.visibility = View.GONE;
        if (position === lista.size - 1) {
            holder.promijeniLokacijuButton.setVisibility(View.VISIBLE)
        }
        holder.imeVakta.text = lista[position].ime
        holder.vrijemeVakta.text = simpleDateFormat.format(lista[position].vrijeme).toString()
        Log.v("TAGIC", simpleDateFormat.format(localTime.time))
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m", Locale.ENGLISH)
        val begin : LocalTime = LocalTime.parse(simpleDateFormat.format(localTime.time), dtf)
        val end : LocalTime = LocalTime.parse(simpleDateFormat.format(lista[position].vrijeme), dtf)
        var duration = Duration.between(begin, end)
        if(duration.isNegative) {
            duration = Duration.between(end, begin)
            var ispisni : String = ""
            if(duration.toHours()<1 && duration.toMinutes()%60<1) ispisni = "par trenutaka"
            else if(duration.toHours()<1) ispisni = "${duration.toMinutes()%60} minuta"
            else if(duration.toHours()>=1) ispisni = "${duration.toHours()} sati i ${duration.toMinutes()%60} minuta"
            holder.relacioniTextView.text = "prije $ispisni"
        }
        else {
            var ispisni : String = ""
            if(duration.toHours()<1 && duration.toMinutes()%60<1) ispisni = "par trenutaka"
            else if(duration.toHours()<1) ispisni = "${duration.toMinutes()%60} minuta"
            else if(duration.toHours()>=1) ispisni = "${duration.toHours()} sati i ${duration.toMinutes()%60} minuta"
            holder.relacioniTextView.text = "za $ispisni"
        }


        for(i in lista.indices) {
            if(LocalTime.parse(simpleDateFormat.format(lista[position].vrijeme), dtf).isAfter(LocalTime.parse(simpleDateFormat.format(localTime.time), dtf))) {
                oboji(holder.imeVakta, holder.vrijemeVakta, holder.relacioniTextView)
                break
            }
        }

        holder.promijeniLokacijuButton.setOnClickListener {
            Log.d("TAGIC", "promijeni lokaciju")
            val intent : Intent = Intent(holder.itemView.context, ListaGradova::class.java)
            intent.putExtra("promjena", true)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size


    inner class VakatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imeVakta : TextView = itemView.findViewById(R.id.imeTextView)
        val vrijemeVakta : TextView = itemView.findViewById(R.id.vrijemeTextView)
        val relacioniTextView : TextView = itemView.findViewById(R.id.relacioniTextView)
        val promijeniLokacijuButton : Button = itemView.findViewById(R.id.button)
    }

    fun getSuffixForNumber(number: Int, maleGender: Boolean): String? {
        val numString = number.toString()
        var suffix = "i"
        var suffixFemale = "i"
        if (number > 10 && number < 15) return if (maleGender) "i" else "a"
        when (numString[numString.length - 1]) {
            '0', '5', '6', '7', '8', '9' -> {
                suffix = "i"
                suffixFemale = "a"
            }
            '1' -> {
                suffix = ""
                suffixFemale = "a"
            }
            '2', '3', '4' -> {
                suffix = "a"
                suffixFemale = "e"
            }
            else -> {
            }
        }
        return if (maleGender) suffix else suffixFemale
    }

    fun oboji(ime : TextView, vrijeme : TextView, relacioni : TextView) {
        ime.setTextColor(Color.YELLOW)
        vrijeme.setTextColor(Color.YELLOW)
        relacioni.setTextColor(Color.YELLOW)
    }
}