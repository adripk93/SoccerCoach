package com.adrian.soccersala

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.adrian.soccersala.game.PlayActivity
import com.adrian.soccersala.jugadores.AddJugador
import com.adrian.soccersala.jugadores.EditJugadoresActivity
import com.adrian.soccersala.jugadores.EstatsJugadoresActivity
import com.adrian.soccersala.game.MatchActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType {
    BASIC
}
class MenuActivity : AppCompatActivity() {

    private lateinit var playButton: Button
    private lateinit var gameButton: Button
    private lateinit var editButton: Button
    private lateinit var seeButton: Button
    private lateinit var closeMenuButton: Button
    private lateinit var teamButton: Button
    private lateinit var ediJugButton: Button
    private lateinit var addButton: Button
    private lateinit var nameTeam: TextView
    private lateinit var name: TextView
    private lateinit var editLnl: LinearLayoutCompat
    private lateinit var changeName: ConstraintLayout
    private lateinit var addTeam: ConstraintLayout
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        /**
         * Guardo los datos de usuario autenticado a nivel de sesion del metodo showMenu de AuthActivity
         * y los iré pasando entre Actividades para controlar el email en toda la aplicacion
         */

        val bundle = intent.extras
        val email: String? = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        initComponents()
        if (email != null) {
            setup(email, "Basic")
        }

        ///Control del botón atrás de Android////
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeSession()
            }
        })

    }

    private fun initComponents() {
        playButton = findViewById(R.id.playButton)
        gameButton = findViewById(R.id.gameButton)
        editButton = findViewById(R.id.editButton)
        seeButton = findViewById(R.id.seeButton)
        closeMenuButton = findViewById(R.id.closeMenuButton)
        nameTeam = findViewById(R.id.teamEdt)
        teamButton = findViewById(R.id.teamButton)
        addTeam = findViewById(R.id.constAddTeam)
        ediJugButton = findViewById(R.id.ediJugButton)
        addButton = findViewById(R.id.addButton)
        name = findViewById(R.id.nameTeamEdt)
        changeName = findViewById(R.id.constChangeName)
        editLnl = findViewById(R.id.editLineal)

    }

    private fun setup(email: String, provider: String) {

        getName(email)
        changeName.isVisible = false
        editLnl.isVisible = false

        val matchIntent = Intent(this, MatchActivity::class.java)
        matchIntent.putExtra("email", email)

        val gameIntent = Intent(this, EstatsJugadoresActivity::class.java)
        gameIntent.putExtra("email", email)

        val addIntent = Intent(this, AddJugador::class.java)
        addIntent.putExtra("email", email)

        val editIntent = Intent(this, EditJugadoresActivity::class.java)
        editIntent.putExtra("email", email)


        //Añadir equipo
        teamButton.setOnClickListener {
            /**
             * Si no se añade ningún nombre se vuelve a la vista del nombre del equipo
             */
            if (nameTeam.text.isEmpty()) {
                Toast.makeText(this, "¡¡¡No has escrito ningún nombre!!!", Toast.LENGTH_LONG).show()
            } else {
                val equipo = nameTeam.text.toString()
                db.collection("users").document(email).set(
                    hashMapOf("equipo" to equipo)
                )
                addTeam.isVisible = false
                changeName.isVisible = true
                name.setText(equipo)
                Toast.makeText(this, "El nombre se ha cambiado correctamente", Toast.LENGTH_SHORT)
                    .show()

            }

        }



        /**
         * Menu del jugador. Dependiendo el boton se manda a una Activity diferente
         */

        ///AÑADIR PARTIDO///
        setClickListenerToButton(playButton)
        {
            db.collection("users").document(email).get().addOnSuccessListener {
                val equipo = it.getString("equipo")
                if(equipo != null){
                    startActivity(matchIntent)
                }else{
                    errorTeam()
                }
            }
        }

        //JUGAR PARTIDO///
        setClickListenerToButton(gameButton)
        {
            db.collection("users").document(email).get().addOnSuccessListener {
                val equipo = it.getString("equipo")
                if(equipo != null){
                    checkMatch(email)
                }else{
                    errorTeam()
                }
            }
        }
        ////EDITAR JUGADORES///

        setClickListenerToButton(editButton)
        {
            editLnl.isVisible = true

        }
        ///AÑADIR JUGADORES///
        setClickListenerToButton(addButton) {
            db.collection("users").document(email).get().addOnSuccessListener {
                val equipo = it.getString("equipo")
                if(equipo != null){
                    startActivity(addIntent)
                }else{
                    errorTeam()
                }
            }

        }

        ///EDITAR JUGADORES///
        setClickListenerToButton(ediJugButton) {
            db.collection("users").document(email).get().addOnSuccessListener {
                val equipo = it.getString("equipo")
                if(equipo != null){
                    startActivity(editIntent)
                }else{
                    errorTeam()
                }
            }
        }
        ///VER ESTADISTICAS DE LOS JUGADORES////
        setClickListenerToButton(seeButton)
        {
            db.collection("users").document(email).get().addOnSuccessListener {
                val equipo = it.getString("equipo")
                if(equipo != null){
                    startActivity(gameIntent)
                }else{
                    errorTeam()
                }
            }
        }

        ////Cerrar sesión///////
        setClickListenerToButton(closeMenuButton)
        {
            closeSession()
        }
    }

    private fun setClickListenerToButton(button: Button, clickAction: () -> Unit) {
        button.setOnClickListener { clickAction() }
    }

    /**
     * Se comprueba si ya existe un equipo en la bbdd.
     * Dependiendo de si existe o no se muestra un ConstraintLayout u ottro
     */
    private fun getName(email: String) {
        db.collection("users").document(email).get().addOnSuccessListener {
            val equipo = it.getString("equipo")
            if (equipo != null) {
                addTeam.isVisible = false
                changeName.isVisible = true
                name.setText(equipo)
            }else{
                addTeam.isVisible = true
                changeName.isVisible = false
            }
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error recuperndo el documento: ", exception)
        }
    }
    ///CERRAR SESIÓN///
    private fun closeSession() {
        AlertDialog.Builder(this@MenuActivity)
            .setMessage("¿Está seguro que quiere salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, whichButton ->
                //Borrado de los datos del usuario
                val prefs =
                    getSharedPreferences(
                        getString(R.string.prefs_file),
                        Context.MODE_PRIVATE
                    ).edit()
                prefs.clear()
                prefs.apply()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }

    /**
     * Función para controlar que hay jugadores convocados
     */
    private fun checkMatch(email: String) {
        db.collection("users").whereEqualTo("isSelected", true).get().addOnSuccessListener {
            if (it != null) {
                val count = it.count()
                if(count<5){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("No se han convocado suficientes jugadores")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }else{
                    val playIntent = Intent(this, PlayActivity::class.java)
                    playIntent.putExtra("email", email)
                    startActivity(playIntent)
                }
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("No hay jugadores convocados")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun errorTeam(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Antes de nada, añade un nombre a tu equipo")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}