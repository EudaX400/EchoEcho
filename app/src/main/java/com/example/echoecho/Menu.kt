package com.example.echoecho


import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Timer
import java.util.TimerTask

class Menu : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var editarBtn: Button
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var edat: TextView
    lateinit var poblacio: TextView
    lateinit var imatgePerfil: ImageView
    lateinit var reference: DatabaseReference
    lateinit var imatgeUri: Uri
    lateinit var storageReference: StorageReference
    var timer= Timer()


    var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tancarSessio = findViewById<Button>(R.id.tancarSessio)
        CreditsBtn = findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn = findViewById<Button>(R.id.jugarBtn)
        editarBtn = findViewById<Button>(R.id.editarBtn)
        puntuacio = findViewById<TextView>(R.id.puntuacio)
        uid = findViewById<TextView>(R.id.uid)
        correo = findViewById<TextView>(R.id.correo)
        nom = findViewById<TextView>(R.id.nom)
        edat = findViewById(R.id.edat)
        poblacio = findViewById(R.id.poblacio)
        imatgePerfil = findViewById(R.id.imatgePerfil)
        storageReference = FirebaseStorage.getInstance().getReference()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        tancarSessio.setOnClickListener {
            tancalaSessio()
        }

        editarBtn.setOnClickListener {
            Toast.makeText(this, "EDITAR", Toast.LENGTH_SHORT).show()
            canviaLaImatge()
        }

        consulta()

        CreditsBtn.setOnClickListener {
            Toast.makeText(this, "Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener {
            Toast.makeText(this, "Puntuacions", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Recyclerview1::class.java)
            startActivity(intent)
        }
        jugarBtn.setOnClickListener {
            Toast.makeText(this, "JUGAR", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Seleccionivell::class.java)
            startActivity(intent)
        }

        CreditsBtn.setOnClickListener { Toast.makeText(this, "Credits", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Credits::class.java)
            startActivity(intent) }

    }


    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun canviaLaImatge() {
        //utilitzarem un alertdialog que seleccionara de galeria o agafar una foto
        // Si volem fer un AlertDialog amb més de dos elements (amb una llista),
        // Aixó ho fariem amb fragments (que veurem més endevant)
        // Aquí hi ha un tutorial per veure com es fa:
        // https://www.codevscolor.com/android-kotlin-list-alert-dialog
        //Veiem com es crea un de dues opcions (habitualment acceptar o cancel·lar:
        val dialog = AlertDialog.Builder(this)
            .setTitle("CANVIAR IMATGE")
            .setMessage("Seleccionar imatge de: ")
            .setNegativeButton("Galeria") { view, _ ->
                Toast.makeText(this, "De galeria", Toast.LENGTH_SHORT).show()
                //mirem primer si tenim permisos per a accedir a Read External Storage
                if (askForPermissions()) {
                    // Ara agafarem una imatge de la galeria
                    val intent = Intent(Intent.ACTION_PICK)
                    val REQUEST_CODE = 201 //Aquest codi és un número que faremservir per
                    // a identificar el que hem recuperat del intent
                    // pot ser qualsevol número
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE)

                } else {
                    Toast.makeText(this, "ERROR PERMISOS", Toast.LENGTH_SHORT).show()
                    val REQUEST_CODE = 201
                    startActivityForResult(intent, REQUEST_CODE)
                }
            }
            .setPositiveButton("Càmera") { view, _ ->
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null){
                    startActivityForResult(intent, 1)

                }else {
                    Toast.makeText(this, "ERROR PERMISOS DE CÁMARA", Toast.LENGTH_SHORT).show()
                }

            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun askForPermissions(): Boolean {
        val REQUEST_CODE = 201
        if (!isPermissionsAllowed()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_CODE
            )
            return false
        } else {
            // Permiso ya concedido
            return true
        }
    }

    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val REQUEST_CODE = 201
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, realiza la operación que necesitas
                    // por ejemplo, en tu caso, puedes llamar a canviaLaImatge()
                    canviaLaImatge()
                } else {
                    // Permiso denegado, muestra el diálogo de permisos denegados
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val REQUEST_CODE_GALLERY = 201
        val REQUEST_CODE_CAMERA = 1

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                // Imagen seleccionada desde la galería
                val imatgeUri = data.data
                imatgeUri?.let {
                    imatgePerfil.setImageURI(imatgeUri)
                    pujarFoto(imatgeUri)
                }
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                // Imagen capturada desde la cámara
                val extras = data.extras
                extras?.let {
                    val imgBitmap = extras.get("data") as Bitmap
                    imatgePerfil.setImageBitmap(imgBitmap)
                    uploadBitmapToFirebase(imgBitmap)
                }
            }
        }
    }

    private fun uploadBitmapToFirebase(bitmap: Bitmap) {
        val folderReference: StorageReference = storageReference.child("Imatge")
        val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val imageReference: StorageReference = folderReference.child("$uid.jpg")

        // Convertir el Bitmap en bytes
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Pujar la imatge a Firebase Storage
        val uploadTask = imageReference.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            // Manejar errors en la càrrega
            Toast.makeText(
                this, "Error en pujar la imatge al magatzem: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { taskSnapshot ->
            // La imatge s'ha pujat correctament
            // Obtenir la URL de la imatge pujada
            imageReference.downloadUrl.addOnSuccessListener { uri ->
                // Guardar la URL de la imatge a la base de dades de Firebase
                guardarUrlImatgeEnFirebase(uid, uri.toString())
            }.addOnFailureListener { exception ->
                // Manejar errors en obtenir la URL de la imatge
                Toast.makeText(
                    this, "Error en obtenir la URL de la imatge: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun pujarFoto(imatgeUri: Uri) {
        val folderReference: StorageReference = storageReference.child("Imatge")
        val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val imageReference: StorageReference = folderReference.child("$uid.jpg")

        // Convertir la imatge en bytes
        val inputStream: InputStream? = contentResolver.openInputStream(imatgeUri)
        inputStream?.let {
            val bitmap = BitmapFactory.decodeStream(it)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Pujar la imatge a Firebase Storage
            val uploadTask = imageReference.putBytes(data)
            uploadTask.addOnFailureListener { exception ->
                // Manejar errors en la càrrega
                Toast.makeText(
                    this, "Error en pujar la imatge al magatzem: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener { taskSnapshot ->
                // La imatge s'ha pujat correctament
                // Obtenir la URL de la imatge pujada
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    // Guardar la URL de la imatge a la base de dades de Firebase
                    guardarUrlImatgeEnFirebase(uid, uri.toString())
                }.addOnFailureListener { exception ->
                    // Manejar errors en obtenir la URL de la imatge
                    Toast.makeText(
                        this, "Error en obtenir la URL de la imatge: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } ?: run {
            // No s'ha pogut obrir l'inputStream
            Toast.makeText(
                this, "Error en obrir la imatge seleccionada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun guardarUrlImatgeEnFirebase(uid: String, imageUrl: String) {
        val database = FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("BASE DE DADES JUGADORS")
        reference.child(uid).child("Imatge").setValue(imageUrl)
            .addOnSuccessListener {
                // La URL de la imatge s'ha guardat correctament a la base de dades
                Toast.makeText(
                    this, "URL de la imatge guardada a Firebase",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { exception ->
                // Manejar errors en guardar la URL de la imatge
                Toast.makeText(
                    this, "Error en guardar la URL de la imatge a Firebase: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }



        private fun usuariLogejat() {
        if (user != null) {
            Toast.makeText(
                this, "Jugador logejat",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun consulta() {
        var database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://echoecho-5e815-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i(
                    "DEBUG", "arrel value" +
                            snapshot.value.toString()
                )
                Log.i("DEBUG", "arrel key" + snapshot.key.toString())
                // ara capturem tots els fills
                var trobat: Boolean = false
                for (ds in snapshot.children) {
                    Log.i("DEBUG", "DS key:" + ds.child("Uid").key.toString())
                    Log.i("DEBUG", "DS value:" + ds.child("Uid").value.toString())
                    Log.i("DEBUG", "DS data:" + ds.child("Data").value.toString())
                    Log.i("DEBUG", "DS mail:" + ds.child("Email").value.toString())
                    //mirem si el mail és el mateix que el del jugador
                    //si és així, mostrem les dades als textview corresponents
                    if
                            (ds.child("Email").value.toString().equals(user?.email)) {
                        trobat = true
//carrega els textview
                        puntuacio.text = ds.child("Puntuacio").value.toString()
                        uid.text = ds.child("Uid").value.toString()
                        correo.text = ds.child("Email").value.toString()
                        nom.text = ds.child("Nom").value.toString()
                        poblacio.text = ds.child("Poblacio").value.toString()
                        edat.text = ds.child("Edat").value.toString()
                        var imatge: String = ds.child("Imatge").value.toString()
                        try {
                            Picasso.get().load(imatge).into(imatgePerfil)
                        } catch (e: Exception) {
                            Picasso.get().load(R.drawable.user).into(imatgePerfil)
                        }

                    }
                    if (!trobat) {
                        Log.e("ERROR", "ERROR NO TROBAT MAIL")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

}
