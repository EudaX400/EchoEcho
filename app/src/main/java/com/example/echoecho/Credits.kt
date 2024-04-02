package com.example.echoecho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import java.util.Timer
import java.util.TimerTask

class Credits : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        fragmentManager = supportFragmentManager

        // Afegir el primer fragment en iniciar l'activitat
        fragmentManager.beginTransaction().apply {
            replace(R.id.frameContainer, FirstFragment())
            commit()
        }

        // Iniciar el canvi automàtic entre fragments
        timer.scheduleAtFixedRate(ChangeFragmentTask(), 0L, 3000L)
    }

    override fun onDestroy() {
        // Aturar el canvi automàtic entre fragments en destruir l'activitat
        timer.cancel()
        super.onDestroy()
    }

    private inner class ChangeFragmentTask : TimerTask() {
        private var isFirstFragmentVisited = true

        override fun run() {
            if (isFirstFragmentVisited) {
                replaceFragment(FirstFragment())
                isFirstFragmentVisited = false
            } else {
                timer.cancel() // Aturar el temporitzador després de visitar el segon fragment
                replaceFragment(SecondFragment())
            }
        }

        private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
            fragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, fragment)
                commit()
            }
        }
    }
}
