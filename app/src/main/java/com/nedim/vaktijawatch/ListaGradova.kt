package com.nedim.vaktijawatch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.ListView

class ListaGradova : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_gradova)

        // Enables Always-on
        setAmbientEnabled()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("postavke",
            Context.MODE_PRIVATE)
        if(sharedPreferences.getInt("grad",0) != 0) {
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val gradoviLista : MutableList<String> = popuniGradovima()
        var gradovi : ListView = findViewById(R.id.gradoviListView)
        gradovi.adapter = ArrayAdapter(
            baseContext,
            android.R.layout.simple_list_item_1,
            gradoviLista
        )

        gradovi.setOnItemClickListener { parent, view, position, id ->

            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("grad", position)
            editor.apply()
            editor.commit()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun popuniGradovima(): MutableList<String> {
        return mutableListOf(
            "Banovići",
            "Banja Luka",
            "Bihać",
            "Bijeljina",
            "Bileća",
            "Bos.Brod",
            "Bos.Dubica",
            "Bos.Gradiška",
            "Bos.Grahovo",
            "Bos.Krupa",
            "Bos.Novi",
            "Bos.Petrovac",
            "Bos.Šamac",
            "Bratunac",
            "Brčko",
            "Breza",
            "Bugojno",
            "Busovača",
            "Bužim",
            "Cazin",
            "Čajniče",
            "Čapljina",
            "Čelić",
            "Čelinac",
            "Čitluk",
            "Derventa",
            "Doboj",
            "Donji Vakuf",
            "Drvar",
            "Foča",
            "Fojnica",
            "Gacko",
            "Glamoč",
            "Goražde",
            "Gornji Vakuf",
            "Gračanica",
            "Gradačac",
            "Grude",
            "Hadžići",
            "Han-Pijesak",
            "Hlivno",
            "Ilijaš",
            "Jablanica",
            "Jajce",
            "Kakanj",
            "Kalesija",
            "Kalinovik",
            "Kiseljak",
            "Kladanj",
            "Ključ",
            "Konjic",
            "Kotor-Varoš",
            "Kreševo",
            "Kupres",
            "Laktaši",
            "Lopare",
            "Lukavac",
            "Ljubinje",
            "Ljubuški",
            "Maglaj",
            "Modriča",
            "Mostar",
            "Mrkonjić-Grad",
            "Neum",
            "Nevesinje",
            "Novi Travnik",
            "Odžak",
            "Olovo",
            "Orašje",
            "Pale",
            "Posušje",
            "Prijedor",
            "Prnjavor",
            "Prozor",
            "Rogatica",
            "Rudo",
            "Sanski Most",
            "Skender-Vakuf",
            "Sokolac",
            "Srbac",
            "Srebrenica",
            "Srebrenik",
            "Stolac",
            "Šekovići",
            "Šipovo",
            "Široki Brijeg",
            "Teslić",
            "Tešanj",
            "Tomislav-Grad",
            "Travnik",
            "Trebinje",
            "Trnovo",
            "Tuzla",
            "Ugljevik",
            "Vareš",
            "V.Kladuša",
            "Visoko",
            "Višegrad",
            "Vitez",
            "Vlasenica",
            "Zavidovići",
            "Zenica",
            "Zvornik",
            "Žepa",
            "Žepče",
            "Živinice",
            "Sarajevo",
            "Bijelo Polje",
            "Gusinje",
            "Nova Varoš",
            "Novi Pazar",
            "Plav",
            "Pljevlja",
            "Priboj",
            "Prijepolje",
            "Rožaje",
            "Sjenica",
            "Tutin"
        )
    }
}