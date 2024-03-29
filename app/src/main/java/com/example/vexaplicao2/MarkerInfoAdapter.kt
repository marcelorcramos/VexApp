package com.example.vexaplicao2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoAdapter(private val context : Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? = null

    override fun getInfoWindow(marker: Marker): View? {
        val place = marker.tag as? Principal.Place ?: return null
     val view= LayoutInflater.from(context).inflate(R.layout.custom2,null)

        view.findViewById<TextView>(R.id.txt_title).text = place.name
        view.findViewById<TextView>(R.id.txt_adress).text = place.address
        view.findViewById<TextView>(R.id.txt_cp).text = place.cp
        view.findViewById<TextView>(R.id.txt_nivel1).text = place.nivel
        view.findViewById<TextView>(R.id.txt_tipo).text = place.tipo
        return view
    }
}