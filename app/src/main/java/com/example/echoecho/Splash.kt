package com.example.echoecho

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.MediaController
import android.widget.VideoView

class Splash : AppCompatActivity() {

    private val duracio: Long = 4500;
    lateinit var videoV: VideoView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //amaguem la barra, pantalla a full
        supportActionBar?.hide()
        //carreguem el video
        videoV = findViewById<VideoView>(R.id.echoVideo)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoV)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.video1
        val uri = Uri.parse(videoPath)
        videoV.setMediaController(mediaController)
        videoV.setVideoURI(uri)
        videoV.requestFocus()
        videoV.start()

        canviarActivity()
    }

    private fun canviarActivity() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, duracio)
    }
}
