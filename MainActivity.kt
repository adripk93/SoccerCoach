package com.adrian.soccersala

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var emailText: EditText
    private lateinit var passText: EditText
    private lateinit var singButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_main)
        // Eventos personalizados en google analytics
        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Integracion en Firebase completada")
        firebaseAnalytics.logEvent("InitScreen", bundle)

        initComponents()
        setup()

    }

    private fun initComponents() {
        emailText = findViewById<EditText>(R.id.emailEditText)
        passText = findViewById<EditText>(R.id.paswordEditText)
        singButton = findViewById<Button>(R.id.signUpButton)
        loginButton = findViewById<Button>(R.id.loginButton)
    }

    private fun setup() {

        /**
         * Boton de acceder
         * Comprobando que los campos no esten vacios, de lo contrario no permite pulsarlo
         */
        singButton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailText.text.toString(),
                    passText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMenu(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlertSignIn()
                    }
                }
            }
        }
        /**
         * Boton de registrarse
         * Comprobando que los campos no esten vacios, de lo contrario no permite pulsarlo
         */
        loginButton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailText.text.toString(),
                    passText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMenu(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlertLogin()
                    }
                }
            }
        }

    }

    /**
     * Control de errores dependiendo si el usuario ha sido registrado previamente o no
     */
    private fun showAlertSignIn() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario ya registrado!!!!")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertLogin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario o la contraseña son incorrectos, intentelo de nuevo")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    /**
     * Función para cambiar a la actividad Menu una vez han iniciado sesion correctamente
     */
    private fun showMenu(email: String, provider: ProviderType) {
        val menuIntent = Intent(this, MenuActivity::class.java)
        menuIntent.putExtra("email", email)
        menuIntent.putExtra("provider", provider)

        startActivity(menuIntent)
    }

}