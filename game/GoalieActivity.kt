package com.adrian.soccersala.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class GoalieActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnMasGol: FloatingActionButton
    private lateinit var btnMenosGol: FloatingActionButton
    private lateinit var goltv: TextView
    private lateinit var btnMasParada: FloatingActionButton
    private lateinit var btnMenosParada: FloatingActionButton
    private lateinit var paradatv: TextView
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
    private var parada: Int = 0
    private var amarilla: Int = 0
    private var roja: Int = 0
    private var penalti: Int = 0
    private var fallado: Int = 0
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goalie)

        val bundle = intent.extras
        val id = bundle?.getString("id")
        val email: String? = bundle?.getString("email")

        initComponents()
        if (email != null) {
            initListener(email)
        }

        setNumbers()

        val back = Intent(this, PlayActivity::class.java)
        back.putExtra("email", email)
            .putExtra("provider", "Basic")

        btnSave.setOnClickListener {
            if (id != null) {
                savePortero(id)
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
        btnMasGol = findViewById(R.id.btnplusGolContrario)
        btnMenosGol = findViewById(R.id.bnminusGolContrario)
        goltv = findViewById(R.id.numberGolContrario)
        btnMasParada = findViewById(R.id.btnplusParada)
        btnMenosParada = findViewById(R.id.bnminusParada)
        paradatv = findViewById(R.id.numberParada)
        btnMasPenalti = findViewById(R.id.btnplusPenaltiParado)
        btnMenosPenalti = findViewById(R.id.bnminusPenaltiParado)
        penaltitv = findViewById(R.id.numberPenaltiParado)
        btnMasFallado = findViewById(R.id.btnplusPenaltiPortero)
        btnMenosFallado = findViewById(R.id.bnminusPenaltiPortero)
        falladotv = findViewById(R.id.numberPenaltiPortero)
        btnMasAmarilla = findViewById(R.id.btnplusAmarillaPortero)
        btnMenosAmarilla = findViewById(R.id.bnminusAmarillaPortero)
        amarillatv = findViewById(R.id.numberAmarillaPortero)
        btnMasRoja = findViewById(R.id.btnplusRojaPortero)
        btnMenosRoja = findViewById(R.id.bnminusRojaPortero)
        rojatv = findViewById(R.id.numberRojaPortero)
        btnSave = findViewById(R.id.savePorteroButton)
        btnCancel = findViewById(R.id.cancelPorteroButton)
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
        btnMasParada.setOnClickListener {
            parada += 1
            setNumbers()
        }

        btnMenosParada.setOnClickListener {
            parada -= 1
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
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("email", email)
                .putExtra("provider", "Basic")
            startActivity(intent)
        }
    }


    private fun setNumbers() {
        goltv.text = gol.toString()
        paradatv.text = parada.toString()
        penaltitv.text = penalti.toString()
        falladotv.text = fallado.toString()
        amarillatv.text = amarilla.toString()
        rojatv.text = roja.toString()

    }

    /**
     * Función para actualizar las estadísticas del portero
     * durante el partido
     */
    private fun savePortero(id: String) {
        /**
         * Utilizo la Transacción de Cloudstore Firebase para asegurarme que
         * dos usuarios no puedan acceder a la vez al mismo portero para modificar
         * las estadísticas
         */
        db.runTransaction { transaction ->
            val snapshot = transaction.get(db.collection("users").document(id))

            if (!snapshot.exists()) {
                throw FirebaseFirestoreException(
                    "Document does not exist!",
                    FirebaseFirestoreException.Code.NOT_FOUND
                )
            }
            val updatedGol = snapshot.getLong("out")?.plus(gol.toLong())
            val updatedParada = snapshot.getLong("parada")?.plus(parada.toLong())
            val updatedPenaltiPortero = snapshot.getLong("penaltiPortero")?.plus(penalti.toLong())
            val updatedPenaltiPorteroFallado = snapshot.getLong("penaltiPorteroFallado")
                ?.plus(fallado.toLong())
            val updatedTarjetaAmarilla = snapshot.getLong("tarjetaAmarilla")
                ?.plus(amarilla.toLong())
            val updatedTarjetaRoja = snapshot.getLong("tarjetaRoja")?.plus(roja.toLong())

            transaction.update(
                db.collection("users").document(id),
                "out", updatedGol,
                "parada", updatedParada,
                "penaltiPortero", updatedPenaltiPortero,
                "penaltiPorteroFallado", updatedPenaltiPorteroFallado,
                "tarjetaAmarilla", updatedTarjetaAmarilla,
                "tarjetaRoja", updatedTarjetaRoja
            )
        }.addOnSuccessListener {
            Toast.makeText(
                this,
                "Se han actualizado las estadísticas",
                Toast.LENGTH_SHORT
            )
                .show()
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this,
                "Error al actualizar las estadísticas: $exception",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    /**
     * Función para volver a poner todos los EditText a 0
     */
    private fun clean() {
        goltv.text = "0"
        paradatv.text = "0"
        penaltitv.text = "0"
        falladotv.text = "0"
        amarillatv.text = "0"
        rojatv.text = "0"
    }


}