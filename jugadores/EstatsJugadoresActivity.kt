package com.adrian.soccersala.jugadores

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.adrian.soccersala.adapter.EstatsJugadorAdapter
import com.google.firebase.firestore.FirebaseFirestore


class EstatsJugadoresActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var jugadorArrayList: ArrayList<Jugador>
    private lateinit var jugadorAdapter: EstatsJugadorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugadores)

        val bundle = intent.extras
        val email: String? = bundle?.getString("email")

        recyclerView = findViewById(R.id.jugadoresRecicler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        jugadorArrayList = arrayListOf()

        if(email!=null){
            eventChangeListener(email)
        }


        val back = Intent(this, MenuActivity::class.java)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back.putExtra("email", email)
                    .putExtra("provider", "BASIC")
                startActivity(back)
            }
        })

    }

    private fun eventChangeListener(email: String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            val equipo = it.getString("equipo")
            db.collection("users").whereEqualTo("equipo", equipo).whereEqualTo("seeIt", true)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val jugador: Jugador = document.toObject(Jugador::class.java)
                        jugadorArrayList.add(jugador)
                        jugadorAdapter = EstatsJugadorAdapter(jugadorArrayList)

                        recyclerView.adapter = jugadorAdapter

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error recuperando el documento: ", exception)
                }
        }

    }
}




