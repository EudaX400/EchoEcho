package com.example.echoecho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class PuntuacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacion)

        val score = intent.getIntExtra("SCORE", 0)
        val textViewPuntuacionValor = findViewById<TextView>(R.id.textViewPuntuacionValor)
        textViewPuntuacionValor.text = score.toString()
    }

    // Function to handle the button click event
    fun returnToMenu(view: View) {
        // Navigate back to the main menu activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to it from the main menu
    }
}

