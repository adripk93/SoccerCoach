package com.adrian.soccersala.jugadores

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.adrian.soccersala.adapter.EditAdpter
import com.google.firebase.firestore.FirebaseFirestore


class EditJugadoresActivity : AppCompatActivity(), EditAdpter.OnButtonClickListener {

    val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var jugadorArrayList: ArrayList<Jugador>
    private lateinit var editAdapter: EditAdpter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_jugadores)
        val bundle = intent.extras
        val email: String? = bundle?.getString("email")

        recyclerView = findViewById(R.id.editJugadoresRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        jugadorArrayList = arrayListOf()

        if (email != null) {
            eventChangeListener(email)
        }


        val back = Intent(this, MenuActivity::class.java)

        ///Control del botón atrás de Android////
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back.putExtra("email", email).putExtra("provider", "BASIC")
                startActivity(back)
            }
        })

    }


    /**
     * Función para mostrar todos los jugadores en el ReciclerView
     */
    private fun eventChangeListener(email: String) {
        db.collection("users").whereEqualTo("seeIt", true).get().addOnSuccessListener { result ->
                for (document in result) {
                    val jugador: Jugador = document.toObject(Jugador::class.java)
                    jugadorArrayList.add(jugador)
                    editAdapter = EditAdpter(jugadorArrayList, this, email)

                    recyclerView.adapter = editAdapter

                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    /**
     * Sobreescribo las funciones de la interfaz para controlar
     * los clicks en los botones del recyclerView
     */

    ///BOTÓN PARA BORRAR EL JUGADOR///
    override fun deleteBtt(id: String?, email: String) {
        val menu = Intent(this, MenuActivity::class.java)
        menu.putExtra("email", email)
            .putExtra("provider", "BASIC")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡¡¡Atención!!!")
            .setMessage("¿Está seguro que quiere eliminar este jugador?")
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->

                db.collection("users").whereEqualTo("id", id).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            //Borrado del jugador
                            db.collection("users").document(id.toString()).delete()
                            startActivity(menu)
                        }

                    }.addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }).setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    ///BOTON PARA EDITAR JUGADOR////
    override fun editBtt(id: String?, email: String) {
        val intent = Intent(this, AddJugador::class.java)
        intent.putExtra("email", email)
            .putExtra("provider", "BASIC")
            .putExtra("id", id)
        db.collection("users").whereEqualTo("id", id).limit(1).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    startActivity(intent)
                }

            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}




