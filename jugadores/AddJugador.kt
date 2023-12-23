package com.adrian.soccersala.jugadores

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.adrian.soccersala.game.PlayActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class AddJugador : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private var isJugadorSelected: Boolean = true
    private var isPorteroSelected: Boolean = false
    private lateinit var viewJugador: CardView
    private lateinit var viewPortero: CardView
    private lateinit var nameEdt: EditText
    private lateinit var apellidoEdt: EditText
    private lateinit var nicknameEdt: EditText
    private lateinit var emailJugador: EditText
    private lateinit var numberEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var imageButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var imagenView: ImageView
    private lateinit var imageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jugador)


        val bundle = intent.extras
        val email: String? = bundle?.getString("email")
        val id: String? = bundle?.getString("id")
        initComponents()
        isSelected()

        if (id != null && email != null) {
            editPlayer(id, email, "Basic")
        } else if (email != null) {
            setup(email, "Basic")
        }

        val back = Intent(this, MenuActivity::class.java)

        ///Control del botón atrás de Android////
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back.putExtra("email", email)
                    .putExtra("provider", "BASIC")
                startActivity(back)
            }
        })
    }

    private fun initComponents() {
        nameEdt = findViewById(R.id.nameJugador)
        apellidoEdt = findViewById(R.id.apellido)
        nicknameEdt = findViewById(R.id.nickname)
        emailJugador = findViewById(R.id.emailJugador)
        numberEditText = findViewById(R.id.numberEditText)
        dateEditText = findViewById(R.id.dateEditText)
        imageButton = findViewById(R.id.chargeImageButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        viewJugador = findViewById(R.id.viewJugador)
        viewPortero = findViewById(R.id.viewPortero)
        imagenView = findViewById(R.id.imagenJugador)

    }

    private fun setup(email: String, provider: String) {

        val menuIntent = Intent(this, MenuActivity::class.java)
        menuIntent.putExtra("email", email)
            .putExtra("provider", provider)

        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        viewJugador.setOnClickListener {
            changePlayer()
            isSelected()
        }
        viewPortero.setOnClickListener {
            changePlayer()
            isSelected()
        }

        imageButton.setOnClickListener {
            seleccionarImagen()
        }


        saveButton.setOnClickListener {
            if (nameEdt.text.isEmpty() || apellidoEdt.text.isEmpty() || nicknameEdt.text.isEmpty()
                || emailJugador.text.isEmpty() || numberEditText.text.isEmpty() || dateEditText.text.isEmpty()
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Faltan campos por rellenar")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                var posicion = ""
                if (isJugadorSelected) {
                    posicion = "jugador"
                } else if (isPorteroSelected) {
                    posicion = "portero"
                }


                /**
                 * Subo la imagen a Storage y añado la Url de la imagen a la base de datos
                 */
                val id = imageUri.lastPathSegment!!
                //Referencia a Storage
                val storageRef = storage.reference.child("Jugadores_IMG")
                    .child(id)
                val uploadTask = storageRef.putFile(imageUri)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        val downloadUri = it.result
                        Toast.makeText(
                            this,
                            "La imagen se ha subido correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        val select = false
                        val see = true

                        //Si se recoge correctamente la URL de la imagen se suben todos los datos a la bd
                        db.collection("users").document(email).get().addOnSuccessListener {
                            val equipo = it.getString("equipo")

                            val player = hashMapOf(
                                "id" to id,
                                "isSelected" to select,
                                "seeIt" to see,
                                "nombre" to nameEdt.text.toString(),
                                "apellido" to apellidoEdt.text.toString(),
                                "nickname" to nicknameEdt.text.toString(),
                                "correo" to emailJugador.text.toString(),
                                "dorsal" to numberEditText.text.toString(),
                                "fecha_nacimiento" to dateEditText.text.toString(),
                                "imagen" to downloadUri.toString(),
                                "posicion" to posicion,
                                "equipo" to equipo
                            )
                            db.collection("users").document(id).set(player, SetOptions.merge())
                        }
                        if (posicion == "jugador") {
                            val jugador = hashMapOf(
                                "gol" to 0,
                                "tiros" to 0,
                                "pases" to 0,
                                "penaltiJugadorMarcado" to 0,
                                "penaltiJugadorFallado" to 0,
                                "tarjetaAmarilla" to 0,
                                "tarjetaRoja" to 0
                            )
                            db.collection("users").document(id).set(jugador, SetOptions.merge())
                        } else {
                            val portero = hashMapOf(
                                "tarjetaAmarilla" to 0,
                                "tarjetaRoja" to 0,
                                "out" to 0,
                                "parada" to 0,
                                "penaltiPortero" to 0,
                                "penaltiPorteroFallado" to 0,
                            )
                            db.collection("users").document(id).set(portero, SetOptions.merge())
                        }

                        Toast.makeText(
                            this,
                            "El jugador se ha añadido con exito",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()

                }


                startActivity(menuIntent)
            }
        }

        cancelButton.setOnClickListener {
            startActivity(menuIntent)
        }

    }


    /**
     * Seleccionar imagen de la galeria y añadirla al imagenView para previsualizarla
     * A partir de Android 13 con ACTION_GET_CONTENT no es necesario pedir los permisos
     * para acceder a la galeria
     */
    private fun seleccionarImagen() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            imagenView.setImageURI(imageUri)
        }
    }


    private fun changePlayer() {
        isJugadorSelected = !isJugadorSelected
        isPorteroSelected = !isPorteroSelected
    }

    /**
     * Se selecciona los colores de los CardView por defecto, seleccionando por defecto a jugador
     */
    private fun isSelected() {
        viewJugador.setCardBackgroundColor(getBackgroundColor(isJugadorSelected))
        viewPortero.setCardBackgroundColor(getBackgroundColor(isPorteroSelected))
    }

    /**
     * Si cambia al seleccionar el tipo de jugador cambia el color
     */
    private fun getBackgroundColor(isSelectedComponent: Boolean): Int {

        val colorReference = if (isSelectedComponent) {
            R.color.backgroun_component_select
        } else {
            R.color.backgroun_component
        }

        return ContextCompat.getColor(this, colorReference)
    }

    /**
     * Función para mostar el calendario y recoger la fecha
     */
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, moth, year -> onDaySelected(day, moth, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    /**
     * Función para mostrar la fecha en el EdtiText
     */
    private fun onDaySelected(day: Int, month: Int, year: Int) {
        dateEditText.setText("$day/$month/$year")
    }

    /**
     * Función para mostrar los datos del jugador si venimos desde la
     * activity de EditJugadores
     */
    private fun editPlayer(id: String, email: String, provider: String) {
        imageButton.isVisible = false
        db.collection("users").document(id).get().addOnSuccessListener {
            val name = it.getString("nombre")
            val apellido = it.getString("apellido")
            val nick = it.getString("nickname")
            val em = it.getString("correo")
            val dorsal = it.getString("dorsal")
            val date = it.getDate("fechaNac")
            val imagen = it.getString("imagen")
            nameEdt.setText(name)
            apellidoEdt.setText(apellido)
            nicknameEdt.setText(nick)
            emailJugador.setText(em)
            numberEditText.setText(dorsal)
            dateEditText.setText(date.toString())
            Picasso.get().load(imagen).into(imagenView)

            val menuIntent = Intent(this, MenuActivity::class.java)
            menuIntent.putExtra("email", email)
                .putExtra("provider", provider)

            dateEditText.setOnClickListener {
                showDatePickerDialog()
            }

            viewJugador.setOnClickListener {
                changePlayer()
                isSelected()
            }
            viewPortero.setOnClickListener {
                changePlayer()
                isSelected()
            }

            saveButton.setOnClickListener {
                if (nameEdt.text.isEmpty() || apellidoEdt.text.isEmpty() || nicknameEdt.text.isEmpty()
                    || emailJugador.text.isEmpty() || numberEditText.text.isEmpty() || dateEditText.text.isEmpty()
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("Faltan campos por rellenar")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    var posicion = ""
                    if (isJugadorSelected) {
                        posicion = "jugador"
                    } else if (isPorteroSelected) {
                        posicion = "portero"
                    }

                    val select = false
                    val see = true

                    db.collection("users").document(email).get().addOnSuccessListener {
                        val equipo = it.getString("equipo")

                        val jugador = hashMapOf(
                            "isSelected" to select,
                            "seeIt" to see,
                            "nombre" to nameEdt.text.toString(),
                            "apellido" to apellidoEdt.text.toString(),
                            "nickname" to nicknameEdt.text.toString(),
                            "correo" to emailJugador.text.toString(),
                            "dorsal" to numberEditText.text.toString(),
                            "fecha_nacimiento" to dateEditText.text.toString(),
                            "posicion" to posicion,
                            "equipo" to equipo
                        )
                        db.collection("users").document(id).set(jugador, SetOptions.merge())
                    }
                    if (posicion == "jugador") {
                        val jugador = hashMapOf(
                            "gol" to 0,
                            "tiros" to 0,
                            "pases" to 0,
                            "penaltiJugadorMarcado" to 0,
                            "penaltiJugadorFallado" to 0,
                            "tarjetaAmarilla" to 0,
                            "tarjetaRoja" to 0
                        )
                        db.collection("users").document(id).set(jugador, SetOptions.merge())
                    } else {
                        val portero = hashMapOf(
                            "tarjetaAmarilla" to 0,
                            "tarjetaRoja" to 0,
                            "out" to 0,
                            "parada" to 0,
                            "penaltiPortero" to 0,
                            "penaltiPorteroFallado" to 0,
                        )
                        db.collection("users").document(id).set(portero, SetOptions.merge())
                    }


                    Toast.makeText(
                        this,
                        "El jugador se ha actualizado con exito",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                startActivity(menuIntent)
            }
            cancelButton.setOnClickListener {
                startActivity(menuIntent)
            }

        }

    }

}

