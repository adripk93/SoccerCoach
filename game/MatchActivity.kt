package com.adrian.soccersala.game

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.adrian.soccersala.adapter.MultiAdapter
import com.adrian.soccersala.jugadores.DatePickerFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MatchActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var fechaEdt: EditText
    private lateinit var enemyEdt: EditText
    private lateinit var jornadaEdt: EditText
    private var localSelected: Boolean = true
    private var visitanteSelected: Boolean = false
    private lateinit var viewLocal: CardView
    private lateinit var viewVisitante: CardView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var jugadorArrayList: ArrayList<Jugador>
    private lateinit var jugadorAdapter: MultiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val bundle = intent.extras
        val email: String? = bundle?.getString("email")

        initComponents()
        isSelectedStadium()

        recyclerView = findViewById(R.id.jugadoresMatchRecicler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        jugadorArrayList = arrayListOf()
        eventChangeListener()

        if (email != null) {
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
        fechaEdt = findViewById(R.id.fechaEdt)
        enemyEdt = findViewById(R.id.teamEnemyEdt)
        jornadaEdt = findViewById(R.id.jornadaEdt)
        viewLocal = findViewById(R.id.viewLocal)
        viewVisitante = findViewById(R.id.viewVisit)
        saveButton = findViewById(R.id.saveMatchButton)
        cancelButton = findViewById(R.id.cancelMatchButton)
    }

    private fun setup(email: String, provider: String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("provider", provider)


        //Recoge la fecha del encuentro///
        fechaEdt.setOnClickListener {
            showDatePickerDialog()
        }

        viewLocal.setOnClickListener {
            changeEstadio()
            isSelectedStadium()
        }

        viewVisitante.setOnClickListener {
            changeEstadio()
            isSelectedStadium()
        }

        //Boton de guardado
        saveButton.setOnClickListener {
            var estadio = ""
            if (localSelected) {
                estadio = "local"
            } else {
                estadio = "visitante"
            }

            if (fechaEdt.text.isEmpty() || enemyEdt.text.isEmpty()) {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Faltan campos por rellenar")
                    .setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                val encuentro = hashMapOf(
                    "fecha_encuentro" to fechaEdt.text.toString(),
                    "jornada" to jornadaEdt.text.toString(),
                    "estadio" to estadio,
                    "equipo_rival" to enemyEdt.text.toString()
                )
                db.collection("users").document(email).set(encuentro, SetOptions.merge())

                Toast.makeText(this, "El partido se ha añadido con existo", Toast.LENGTH_SHORT)
                    .show()

                startActivity(intent)
            }
        }
        //Boton de cancelar
        cancelButton.setOnClickListener {
            startActivity(intent)
        }
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
        fechaEdt.setText("$day/$month/$year")
    }


    private fun changeEstadio() {
        localSelected = !localSelected
        visitanteSelected = !visitanteSelected
    }

    /**
     * Se selecciona los colores de los CardView por defecto, seleccionando por defecto a local
     */
    private fun isSelectedStadium() {
        viewLocal.setCardBackgroundColor(getBackgroundColor(localSelected))
        viewVisitante.setCardBackgroundColor(getBackgroundColor(visitanteSelected))
    }

    /**
     * Cambio de color según la elección del usuario del estadio
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
     * Función para mostrar los jugadores en el ReciclerView
     */
    private fun eventChangeListener() {
        db.collection("users").whereEqualTo("seeIt", true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val jugador: Jugador = document.toObject(Jugador::class.java)
                    jugadorArrayList.add(jugador)
                    jugadorAdapter = MultiAdapter(jugadorArrayList)

                    recyclerView.adapter = jugadorAdapter

                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error recuperando el documento: ", exception)
            }
    }

}