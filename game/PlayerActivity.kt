package com.adrian.soccersala.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.adrian.soccersala.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class PlayerActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnMasGol: FloatingActionButton
    private lateinit var btnMenosGol: FloatingActionButton
    private lateinit var goltv: TextView
    private lateinit var btnMasTiro: FloatingActionButton
    private lateinit var btnMenosTiro: FloatingActionButton
    private lateinit var tirotv: TextView
    private lateinit var btnMasPases: FloatingActionButton
    private lateinit var btnMenosPases: FloatingActionButton
    private lateinit var pasestv: TextView
    private lateinit var btnMasPenalti: FloatingActionButton
    private lateinit var btnMenosPenalti: FloatingActionButton
    private lateinit var penaltitv: TextView
    private lateinit var btnMasFallado: FloatingActionButton
    private lateinit var btnMenosFallado: FloatingActionButton
    private lateinit var falladotv: TextView
    private lateinit var btnMasAmarilla: FloatingActionButton
    private lateinit var btnMenosAmarilla: FloatingActionButton
    private lateinit var amarillatv: TextView
    private lateinit var btnMasRoja: FloatingActionButton
    private lateinit var btnMenosRoja: FloatingActionButton
    private lateinit var rojatv: TextView
    private var gol: Int = 0
    private var tiros: Int = 0
    private var pases: Int = 0
    private var amarilla: Int = 0
    private var roja: Int = 0
    private var penalti: Int = 0
    private var fallado: Int = 0
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val bundle = intent.extras
        val id = bundle?.getString("id")
        val email: String? = bundle?.getString("email")

        initComponents()
        if (email != null) {
            initListener(email)
        }

        setNumbers()

        val back = Intent(this, PlayActivity::class.java)
        back.putExtra("email", email).putExtra("provider", "Basic")
        btnSave.setOnClickListener {
            if (id != null && email != null) {
                saveJugador(id)
                clean()
                startActivity(back)
            }
        }
        ///Control del botón atrás de Android////
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(back)
            }
        })

    }

    private fun initComponents() {
        btnMasGol = findViewById(R.id.btnplusGol)
        btnMenosGol = findViewById(R.id.bnminusGol)
        goltv = findViewById(R.id.numberGol)
        btnMasPases = findViewById(R.id.btnplusPases)
        btnMenosPases = findViewById(R.id.bnminusPases)
        pasestv = findViewById(R.id.numberPases)
        btnMasTiro = findViewById(R.id.btnplusTiros)
        btnMenosTiro = findViewById(R.id.bnminusTiros)
        tirotv = findViewById(R.id.numberTiros)
        btnMasPenalti = findViewById(R.id.btnplusPenalti)
        btnMenosPenalti = findViewById(R.id.bnminusPenalti)
        penaltitv = findViewById(R.id.numberPenaltiMarcado)
        btnMasFallado = findViewById(R.id.btnplusFallado)
        btnMenosFallado = findViewById(R.id.bnminusFallado)
        falladotv = findViewById(R.id.numberFallado)
        btnMasAmarilla = findViewById(R.id.btnplusAmarillaJugador)
        btnMenosAmarilla = findViewById(R.id.bnminusAmarillaJugador)
        amarillatv = findViewById(R.id.numberAmarillaJugador)
        btnMasRoja = findViewById(R.id.btnplusRojaJugador)
        btnMenosRoja = findViewById(R.id.bnminusRojaJugador)
        rojatv = findViewById(R.id.numberRojaJugador)
        btnSave = findViewById(R.id.saveJugadorButton)
        btnCancel = findViewById(R.id.cancelJugadorButton)

    }

    private fun initListener(email: String) {
        btnMasGol.setOnClickListener {
            gol += 1
            setNumbers()
        }

        btnMenosGol.setOnClickListener {
            gol -= 1
            setNumbers()

        }
        btnMasTiro.setOnClickListener {
            tiros += 1
            setNumbers()
        }

        btnMenosTiro.setOnClickListener {
            tiros -= 1
            setNumbers()

        }
        btnMasPases.setOnClickListener {
            pases += 1
            setNumbers()
        }

        btnMenosPases.setOnClickListener {
            pases -= 1
            setNumbers()

        }
        btnMasPenalti.setOnClickListener {
            penalti += 1
            setNumbers()
        }

        btnMenosPenalti.setOnClickListener {
            penalti -= 1
            setNumbers()

        }
        btnMasFallado.setOnClickListener {
            fallado += 1
            setNumbers()
        }

        btnMenosFallado.setOnClickListener {
            fallado -= 1
            setNumbers()

        }
        btnMasAmarilla.setOnClickListener {
            amarilla += 1
            setNumbers()
        }

        btnMenosAmarilla.setOnClickListener {
            amarilla -= 1
            setNumbers()

        }
        btnMasRoja.setOnClickListener {
            roja += 1
            setNumbers()
        }

        btnMenosRoja.setOnClickListener {
            roja -= 1
            setNumbers()

        }

        btnCancel.setOnClickListener {
            val cancel = Intent(this, PlayActivity::class.java)
            cancel.putExtra("email", email)
            cancel.putExtra("provider", "Basic")
            startActivity(cancel)
        }
    }

    private fun setNumbers() {
        goltv.text = gol.toString()
        tirotv.text = tiros.toString()
        pasestv.text = pases.toString()
        penaltitv.text = penalti.toString()
        falladotv.text = fallado.toString()
        amarillatv.text = amarilla.toString()
        rojatv.text = roja.toString()

    }

    /**
     * Función para actualizar las estadísticas del jugador
     * durante el partido
     */
    private fun saveJugador(id: String) {
        /**
         * Utilizo la Transacción de Cloudstore Firebase para asegurarme que
         * dos usuarios no puedan acceder a la vez al mismo jugador para modificar
         * las estadísticas
         */
        db.runTransaction { transaction ->
            val snapshot = transaction.get(db.collection("users").document(id))

            if (!snapshot.exists()) {
                throw FirebaseFirestoreException(
                    "Document does not exist!", FirebaseFirestoreException.Code.NOT_FOUND
                )
            }
            val updatedGol = snapshot.getLong("gol")?.plus(gol.toLong())
            val updatedTiros = snapshot.getLong("tiros")?.plus(tiros.toLong())
            val updatedPases = snapshot.getLong("pases")?.plus(pases.toLong())
            val updatedPenaltiJugadorMarcado =
                snapshot.getLong("penaltiJugadorMarcado")?.plus(penalti.toLong())
            val updatedPenaltiJugadorFallado =
                snapshot.getLong("penaltiJugadorFallado")?.plus(fallado.toLong())
            val updatedTarjetaAmarilla =
                snapshot.getLong("tarjetaAmarilla")
            val updatedTarjetaRoja = snapshot.getLong("tarjetaRoja")?.plus(roja.toLong())

            transaction.update(
                db.collection("users").document(id),
                "gol", updatedGol,
                "tiros", updatedTiros,
                "pases", updatedPases,
                "penaltiJugadorMarcado", updatedPenaltiJugadorMarcado,
                "penaltiJugadorFallado", updatedPenaltiJugadorFallado,
                "tarjetaAmarilla", updatedTarjetaAmarilla,
                "tarjetaRoja", updatedTarjetaRoja
            )
        }.addOnSuccessListener {
            Toast.makeText(
                this, "Se han actualizado las estadísticas", Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this, "Error al actualizar las estadísticas: $exception", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Función para volver a poner todos los EditText a 0
     */
    private fun clean() {
        goltv.text = "0"
        tirotv.text = "0"
        pasestv.text = "0"
        penaltitv.text = "0"
        falladotv.text = "0"
        amarillatv.text = "0"
        rojatv.text = "0"
    }

}