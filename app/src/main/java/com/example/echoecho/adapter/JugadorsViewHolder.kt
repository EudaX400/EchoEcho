package com.example.echoecho.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.echoecho.Jugador
import com.example.echoecho.R
import com.squareup.picasso.Picasso


class JugadorsViewHolder (view: View): RecyclerView.ViewHolder(view) {
    //afegim les variables que apunten als continguts del layout
    val nomJugador=view.findViewById<TextView>(R.id.tvNom_Jugador)
    val puntuacioJugador=view.findViewById<TextView>(R.id.tvPuntuacio_Jugador)
    val foto=view.findViewById<ImageView>(R.id.ivJugador)

    fun render(jugadorModel: Jugador){
        //la cridara per a cada jugador
        nomJugador.text=jugadorModel.nom_jugador
        puntuacioJugador.text=jugadorModel.puntuacio.toString() //recordem que és un int
        Picasso.get().load(jugadorModel.foto).resize(150,150).into(foto)

    }
}
