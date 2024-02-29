package com.example.echoecho

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.*
import java.util.ArrayList
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private val imageArray = ArrayList<ImageView>()
    private val sequence = ArrayList<ImageView>()
    private var current = 0
    private lateinit var mediaPlayerPiano: MediaPlayer
    private lateinit var mediaPlayerGuitar: MediaPlayer
    private lateinit var mediaPlayerMaracas: MediaPlayer
    private lateinit var mediaPlayerTambor: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mediaPlayerPiano = MediaPlayer.create(this, R.raw.piano)
        mediaPlayerGuitar = MediaPlayer.create(this, R.raw.guitar)
        mediaPlayerMaracas = MediaPlayer.create(this, R.raw.maracas)
        mediaPlayerTambor = MediaPlayer.create(this, R.raw.tambor)

        imageArray.add(findViewById(R.id.imageView1))
        imageArray.add(findViewById(R.id.imageView2))
        imageArray.add(findViewById(R.id.imageView3))
        imageArray.add(findViewById(R.id.imageView4))

        launchOnMain {
            delay(1000)
            showSequence(true)
        }
    }

    private fun launchOnMain(block: suspend CoroutineScope.() -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            block()
        }
    }

    private fun makeSequence() {
        val randImg = Random.nextInt(0, 4)
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
            imageArray[3] -> mediaPlayerMaracas.start()
        }
    }

    private fun showSequence(add: Boolean) {
        GlobalScope.launch(Dispatchers.Default) {
            if (add)
                makeSequence()

            for (i in sequence) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    shakeImage(i)
                }
            }
        }
    }

    fun detect(view: View) {
        val imgPressed = findViewById<ImageView>(view.id)

        shakeImage(imgPressed)

        if (imgPressed == sequence[current]) {
            current += 1
            if (sequence.count() - 1 < current) {
                current = 0
                showSequence(true)
            }
        } else {
            val mainMenu = Intent(this@GameActivity, MainActivity::class.java)
            startActivity(mainMenu)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerPiano.release()
        mediaPlayerGuitar.release()
        mediaPlayerMaracas.release()
        mediaPlayerTambor.release()
    }
}
