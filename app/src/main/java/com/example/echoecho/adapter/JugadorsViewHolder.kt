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

    fun render(jugador: Jugador){
        nomJugador.text = jugador.nom_jugador
        puntuacioJugador.text = jugador.puntuacio.toString()
        if (jugador.foto.isNotEmpty()) {
            Picasso.get().load(jugador.foto).resize(150,150).into(foto)
        } else {
            // Si la URL de la imagen está vacía, mostrar una imagen predeterminada
            Picasso.get().load(R.drawable.user).resize(150,150).into(foto)
        }
    }
}
