package com.example.echoecho

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private val imageArray = ArrayList<ImageView>()
    private val sequence = ArrayList<ImageView>()
    private var current = 0
    private var puntsactuals: Int = 0
    private lateinit var puntuacion: TextView

    private lateinit var mediaPlayerPiano: MediaPlayer
    private lateinit var mediaPlayerGuitar: MediaPlayer
    private lateinit var mediaPlayerTambor: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        database = FirebaseDatabase.getInstance().reference.child("Puntuacio")
        mediaPlayerPiano = MediaPlayer.create(this, R.raw.piano)
        mediaPlayerGuitar = MediaPlayer.create(this, R.raw.guitar)
        mediaPlayerTambor = MediaPlayer.create(this, R.raw.tambor)

        puntuacion = findViewById(R.id.puntuacion)
        puntuacion.text = puntsactuals.toString()

        imageArray.add(findViewById(R.id.imageView1))
        imageArray.add(findViewById(R.id.imageView2))
        imageArray.add(findViewById(R.id.imageView3))

        launchOnMain {
            delay(1000)
            showSequence(true)
        }
    }

    private fun contador(increment: Int) {
        puntsactuals += increment
        puntuacion.text = puntsactuals.toString()
    }

    private fun launchOnMain(block: suspend CoroutineScope.() -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            block()
        }
    }

    private fun makeSequence() {
        val randImg = Random.nextInt(0, imageArray.size)
        sequence.add(imageArray[randImg])
    }

    private fun shakeImage(image: ImageView) {
        val rotation1 = ObjectAnimator.ofFloat(image, View.ROTATION_Y, -40f)
        rotation1.duration = 200
        rotation1.start()

        rotation1.doOnEnd {
            val rotation2 = ObjectAnimator.ofFloat(image, View.ROTATION_Y, 40f)
            rotation2.duration = 200
            rotation2.start()

            rotation2.doOnEnd {
                val rotation3 = ObjectAnimator.ofFloat(image, View.ROTATION_Y, 0f)
                rotation3.duration = 200
                rotation3.start()
            }
        }

        when (image) {
            imageArray[0] -> mediaPlayerTambor.start()
            imageArray[1] -> mediaPlayerGuitar.start()
            imageArray[2] -> mediaPlayerPiano.start()
        }
    }

    private var primeraSecuencia = true

    private fun showSequence(add: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            if (add)
                makeSequence()

            for (i in sequence) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    shakeImage(i)
                }
            }

            // Incrementar la puntuación solo si no es la primera secuencia
            if (!primeraSecuencia) {
                contador(10)
            } else {
                primeraSecuencia = false
            }
        }
    }

    fun detect(view: View) {
        val imgPressed = findViewById<ImageView>(view.id)
        shakeImage(imgPressed)

        if (imgPressed == sequence[current]) {
            current += 1
            if (sequence.count() - 1 < current) {
                // El usuario ha completado la secuencia correctamente
                showSequence(true)
                current = 0 // Reiniciar el índice
            }
        } else {
            // El usuario ha fallado
            saveScoreAndShowResult()
            // Reiniciar el juego
        }
    }

    private fun saveScoreAndShowResult() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userScoreRef =
                FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
                    .reference
                    .child("DATA BASE JUGADORS")
                    .child(uid)
                    .child("Puntuacio")

            // Retrieve the existing score from the database
            userScoreRef.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val existingScoreStr = dataSnapshot.getValue(String::class.java) ?: "0"
                    val existingScore = existingScoreStr.toIntOrNull() ?: 0

                    // Add the new score to the existing score
                    val newScore = existingScore + puntsactuals

                    // Save the new score as a string to the database
                    userScoreRef.setValue(newScore.toString())

                    // Display the total score in the UI
                    CoroutineScope(Dispatchers.Main).launch {
                        setContentView(R.layout.activity_puntuacion)
                        val textViewPuntuacionValor =
                            findViewById<TextView>(R.id.textViewPuntuacionValor)
                        textViewPuntuacionValor.text = puntsactuals.toString()

                        // Handle the button click event to return to the main menu
                        findViewById<Button>(R.id.buttonVolver).setOnClickListener {
                            val intent = Intent(this@GameActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Finish the current activity to prevent going back to it from the main menu
                        }
                    }
                } else {
                    // If the score node doesn't exist, set the new score as the initial score
                    userScoreRef.setValue(puntsactuals.toString())

                    // Display the new score in the UI
                    CoroutineScope(Dispatchers.Main).launch {
                        setContentView(R.layout.activity_puntuacion)
                        val textViewPuntuacionValor =
                            findViewById<TextView>(R.id.textViewPuntuacionValor)
                        textViewPuntuacionValor.text = puntsactuals.toString()

                        // Handle the button click event to return to the main menu
                        findViewById<Button>(R.id.buttonVolver).setOnClickListener {
                            val intent = Intent(this@GameActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Finish the current activity to prevent going back to it from the main menu
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerPiano.release()
        mediaPlayerGuitar.release()
        mediaPlayerTambor.release()
    }
}
