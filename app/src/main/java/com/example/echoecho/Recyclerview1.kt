package com.example.echoecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echoecho.adapter.JugadorsAdapter


class Recyclerview1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview1)
        initRecyclerView()

    }

    fun initRecyclerView(){
        val recyclerView=findViewById<RecyclerView>(R.id.RecyclerOne)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= JugadorsAdapter(Jugador.jugadors)
    }

}