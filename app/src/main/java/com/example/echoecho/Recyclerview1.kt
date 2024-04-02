package com.example.echoecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echoecho.adapter.JugadorsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Recyclerview1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview1)

        // Obtener datos de Firebase y configurar RecyclerView
        obtenirPuntuacio()
    }

    private fun obtenirPuntuacio() {
        val database = FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
        val bdreference = database.getReference("DATA BASE JUGADORS")

        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jugadores = mutableListOf<Jugador>()
                for (ds in snapshot.children) {
                    val nomJugador = ds.child("Nom").getValue(String::class.java) ?: ""
                    val puntuacionStr = ds.child("Puntuacio").getValue(String::class.java) ?: "0"
                    val puntuacion = puntuacionStr.toIntOrNull() ?: 0
                    val foto = ds.child("Imatge").getValue(String::class.java) ?: ""
                    val jugador = Jugador(nomJugador, puntuacion, foto)
                    jugadores.add(jugador)
                }

                // Inicializar y configurar RecyclerView con los datos obtenidos de Firebase
                val recyclerView = findViewById<RecyclerView>(R.id.RecyclerOne)
                recyclerView.layoutManager = LinearLayoutManager(this@Recyclerview1)
                recyclerView.adapter = JugadorsAdapter(jugadores)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

}
