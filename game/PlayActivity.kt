package com.adrian.soccersala.game

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.adrian.soccersala.adapter.PlayAdapter
import com.adrian.soccersala.model.Match
import com.google.firebase.firestore.FirebaseFirestore

class PlayActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var jugadorArrayList: ArrayList<Jugador>
    private lateinit var playAdapter: PlayAdapter
    private lateinit var jornada :EditText
    private lateinit var team : EditText
    private lateinit var enemy : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val bundle = intent.extras
        val email: String? = bundle?.getString("email")

        initComponents()


        recyclerView = findViewById(R.id.jugadoresPlayRecicler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        jugadorArrayList = arrayListOf()


        if (email != null) {
            eventChangeListener(email, "Basic")
            setup(email)

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

    private fun initComponents(){
        jornada= findViewById(R.id.numberJornada)
        team=findViewById(R.id.teamName)
        enemy=findViewById(R.id.enemyTeam)
    }
    /**
     * Función para pintar los jugadores convocados al partido
     */
    private fun eventChangeListener(email: String, provider: String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            val equipo = it.getString("equipo")
            db.collection("users").whereEqualTo("equipo", equipo).whereEqualTo("isSelected", true)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val jugador: Jugador = document.toObject(Jugador::class.java)
                        jugadorArrayList.add(jugador)
                        playAdapter = PlayAdapter(jugadorArrayList, { showGame(it, email, provider) })

                        recyclerView.adapter = playAdapter

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error recuperndo el documento: ", exception)
                }
        }

    }

    /**
     * Función lambda para pasar de actividad según el elemento del
     * RecyclerView que se pulse
     */
    private fun showGame(jugador: Jugador, email: String, provider: String) {
        val id: String = jugador.id.toString()
        val jugadorIntent = Intent(this, PlayerActivity::class.java)
        jugadorIntent.putExtra("id", id)
            .putExtra("email", email)
            .putExtra("provider", provider)
        val porteroIntent = Intent(this, GoalieActivity::class.java)
        porteroIntent.putExtra("id", id)
            .putExtra("email", email)
            .putExtra("provider", provider)

        if (jugador.posicion == "jugador") {
            startActivity(jugadorIntent)
        } else {
            startActivity(porteroIntent)

        }
    }

    private fun setup(email: String){
        db.collection("users").document(email).get().addOnSuccessListener {

            if (it != null) {
                val equipo = it.getString("equipo")
                val enemigo = it.getString("equipo_rival")
                val jor = it.getString("jornada")
                jornada.setText(jor)
                team.setText(equipo)
                enemy.setText(enemigo)
            }
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error recuperndo el documento: ", exception)
        }
    }

}
