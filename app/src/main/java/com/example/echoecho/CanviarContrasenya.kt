package com.example.echoecho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CanviarContrasenya : AppCompatActivity() {

    private lateinit var passCanvi: EditText
    private lateinit var canviarContrasenya: Button
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canviar_contrasenya)

        passCanvi = findViewById(R.id.passCanvi)
        canviarContrasenya = findViewById(R.id.canviarContrasenya)

        val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        canviarContrasenya.setOnClickListener {
            val novaPassword: String = passCanvi.text.toString()

            if (novaPassword.length < 6) {
                passCanvi.error = "Password must be at least 6 characters long"
            } else {
                canviarPassword(uid, novaPassword)
            }
        }
    }

    private fun canviarPassword(uid: String, novaPassword: String) {
        val database = FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("DATA BASE JUGADORS")
        reference.child(uid).child("Password").setValue(novaPassword)
            .addOnSuccessListener {
                Toast.makeText(this, "Contrasenya canviada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al canviar la contrasenya", Toast.LENGTH_SHORT).show()
            }
    }
}