package com.example.echoecho

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Menu : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom:TextView
    lateinit var reference: DatabaseReference
    var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tancarSessio = findViewById<Button>(R.id.tancarSessio)
        CreditsBtn = findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn = findViewById<Button>(R.id.jugarBtn)
        puntuacio = findViewById<TextView>(R.id.puntuacio)
        uid = findViewById<TextView>(R.id.uid)
        correo = findViewById<TextView>(R.id.correo)
        nom = findViewById<TextView>(R.id.nom)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        tancarSessio.setOnClickListener {
            tancalaSessio()
        }

        consulta()

        CreditsBtn.setOnClickListener {
            Toast.makeText(this, "Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener {
            Toast.makeText(this, "Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener {
            Toast.makeText(this, "JUGAR", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Seleccionivell::class.java)
            startActivity(intent)
        }


    }


    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun usuariLogejat() {
        if (user != null) {
            Toast.makeText(
                this, "Jugador logejat",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun consulta() {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i(
                    "DEBUG", "arrel value" +
                            snapshot.value.toString()
                )
                Log.i("DEBUG", "arrel key" + snapshot.key.toString())
                // ara capturem tots els fills
                var trobat: Boolean = false
                for (ds in snapshot.children) {
                    Log.i("DEBUG", "DS key:" + ds.child("Uid").key.toString())
                    Log.i("DEBUG", "DS value:" + ds.child("Uid").value.toString())
                    Log.i("DEBUG", "DS data:" + ds.child("Data").value.toString())
                    Log.i("DEBUG", "DS mail:" + ds.child("Email").value.toString())
                    //mirem si el mail és el mateix que el del jugador
                    //si és així, mostrem les dades als textview corresponents
                    if
                            (ds.child("Email").value.toString().equals(user?.email)) {
                        trobat = true
//carrega els textview
                        puntuacio.setText(
                            ds.child("Puntuacio").value.toString()
                        )
                        uid.setText(
                            ds.child("Uid").value.toString()
                        )
                        correo.setText(
                            ds.child("Email").value.toString()
                        )
                        nom.setText(
                            ds.child("Nom").value.toString()
                        )
                    }
                    if (!trobat) {
                        Log.e("ERROR", "ERROR NO TROBAT MAIL")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

}
