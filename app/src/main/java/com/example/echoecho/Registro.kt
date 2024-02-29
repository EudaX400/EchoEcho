package com.example.echoecho

import android.content.Intent
import android.icu.text.DateFormat.getDateInstance
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {

    lateinit var correoEt: EditText
    lateinit var passEt: EditText
    lateinit var nombreEt: EditText
    lateinit var fechaTxt: TextView
    lateinit var Registrar: Button

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        correoEt = findViewById<EditText>(R.id.correoEt)
        passEt = findViewById<EditText>(R.id.passEt)
        nombreEt = findViewById<EditText>(R.id.nombreEt)
        fechaTxt = findViewById<TextView>(R.id.fechaTxt)
        Registrar = findViewById<Button>(R.id.Registrar)
        auth = FirebaseAuth.getInstance()


        //carreguem la data al TextView
        //Utilitzem calendar (hi ha moltes altres opcions)
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use
        getDateInstance()
        val formatedDate = formatter.format(date)
        //ara la mostrem al TextView
        fechaTxt.text = formatedDate


        Registrar.setOnClickListener {
            //Abans de fer el registre validem les dades
            var email: String = correoEt.text.toString()
            var pass: String = passEt.text.toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoEt.error = "Invalid Mail"
            } else if (pass.length < 6) {
                passEt.error = "Password less than 6 chars"
            } else {
                registrarJugador(email, pass)
            }

        }
    }
    private fun registrarJugador(email: String, passw: String) {

        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this, "createUserWithEmail:success", Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed .", Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }

    }
    fun updateUI(user: FirebaseUser?) {
        //hi ha un interrogant perquè podria ser null
        if (user != null) {
            var puntuacio: Int = 0
            var uidString: String = user.uid
            var correoString: String = correoEt.text.toString()
            var passString: String = passEt.text.toString()
            var nombreString: String = nombreEt.text.toString()
            var fechaString: String = fechaTxt.text.toString()
            //AQUI GUARDA EL CONTINGUT A LA BASE DE DADES

            var dadesJugador : HashMap<String,String> = HashMap<String, String>()
            dadesJugador.put ("Uid",uidString)
            dadesJugador.put ("Email",correoString)
            dadesJugador.put ("Password",passString)
            dadesJugador.put ("Nom",nombreString)
            dadesJugador.put ("Data",fechaString)
            dadesJugador.put ("Puntuacio", puntuacio.toString())

            var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
                if(reference!=null) {
                    //crea un fill amb els valors de dadesJugador
                    reference.child(uidString).setValue(dadesJugador)
                    Toast.makeText(this, "USUARI BEN REGISTRAT",
                        Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "ERROR BD", Toast.LENGTH_SHORT).show()
                }
                        finish()
// FALTA FER
        } else {
            Toast.makeText(
                this, "ERROR CREATE USER ", Toast.LENGTH_SHORT
            ).show()
        }
    }
}