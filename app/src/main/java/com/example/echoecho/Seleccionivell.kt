package com.example.echoecho

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Seleccionivell : AppCompatActivity() {

    lateinit var level1: Button
    lateinit var level2: Button
    lateinit var level3: Button

    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private var NIVELL: String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionivell)

        level1 = findViewById<Button>(R.id.level1)
        level2 = findViewById<Button>(R.id.level2)
        level3 = findViewById<Button>(R.id.level3)


        level1.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        level2.setOnClickListener {
            val intent = Intent(this, GameActivity2::class.java)
            startActivity(intent)
        }

        level3.setOnClickListener {
            val intent = Intent(this, GameActivity3::class.java)
            startActivity(intent)
        }

    }
}