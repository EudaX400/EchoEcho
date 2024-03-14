package com.example.echoecho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

data class Jugador(val nom_jugador: String, val puntuacio: Int, val foto: String) :
    AppCompatActivity() {
    companion object {
        val jugadors = listOf<Jugador>(
            Jugador("Pepe", 2, "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/eb5b302a-789e-436f-a408-e9716e20f324/da8qomf-87f2c9a6-d20e-4a43-b115-365e35d3bc1f.png/v1/fill/w_1024,h_709,q_80,strp/shawn_frost__inazuma_eleven__by_nash_i_da8qomf-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NzA5IiwicGF0aCI6IlwvZlwvZWI1YjMwMmEtNzg5ZS00MzZmLWE0MDgtZTk3MTZlMjBmMzI0XC9kYThxb21mLTg3ZjJjOWE2LWQyMGUtNGE0My1iMTE1LTM2NWUzNWQzYmMxZi5wbmciLCJ3aWR0aCI6Ijw9MTAyNCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.lehH0FmCoyjRTHI4PxYo0wGmrxzQ0ApqJjGoNYyVzdQ"),
            Jugador("Juan", 3, "https://w0.peakpx.com/wallpaper/4/72/HD-wallpaper-anime-inazuma-eleven-hakuryuu-inazuma-eleven.jpg"),
            Jugador("María", 4, "https://pbncanvas.com/wp-content/uploads/2021/10/Ezio-Auditore-da-Firenze-paint-by-number.jpg"),
            Jugador("Luis", 5, "foto4.jpg")

        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionivell)

    }

}

