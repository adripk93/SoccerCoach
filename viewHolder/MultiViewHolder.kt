package com.adrian.soccersala.viewHolder

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.R
import com.adrian.soccersala.model.Jugador
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class MultiViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Referencia a los objetos del layout item_multi
     * que se le pasaran a MultiAdapter.kt
     * para mostrar a los jugadores que se pueden elegir
     * en la actividad MatchActivity
     */
    private val db = FirebaseFirestore.getInstance()
    val nameMulti = view.findViewById<TextView>(R.id.nameMultiTv)
    val dorsal = view.findViewById<TextView>(R.id.dorsalMultiTv)
    val tarjetaYellow = view.findViewById<TextView>(R.id.tarjetYelloTv)
    val tarjetaRed = view.findViewById<TextView>(R.id.tarjetRedTv)
    val linearJug = view.findViewById<LinearLayoutCompat>(R.id.linearJugador)
    val linearPor = view.findViewById<LinearLayoutCompat>(R.id.linearPortero)
    val golMilti = view.findViewById<TextView>(R.id.golMultiTv)
    val tirosMulti = view.findViewById<TextView>(R.id.tirosMultiTv)
    val paradasMulti = view.findViewById<TextView>(R.id.paradasMultiTv)
    val imagenMulti = view.findViewById<ImageView>(R.id.jugadorMultiImV)
    val click = view.findViewById<CheckBox>(R.id.checkBox)


    fun render(item: Jugador) {

        if (item.posicion == "jugador") {
            linearPor.isVisible = false
            golMilti.text = item.gol.toString()
            tirosMulti.text = item.tiros.toString()

        } else {
            linearJug.isVisible = false
            paradasMulti.text = item.parada.toString()
        }
        nameMulti.text = item.nickname.toString()
        dorsal.text = item.dorsal.toString()
        tarjetaYellow.text = item.tarjetaAmarilla.toString()
        tarjetaRed.text = item.tarjetaRoja.toString()
        Picasso.get().load(item.imagen).into(imagenMulti)
        click.setOnClickListener{
            if(!it.isSelected) {
                selectJugador(item)
            }else{
               noSelect(item)
            }

        }
    }

    /**
     * Si el usuario selecciona al jugador en la actividad del Match
     * el jugador cambia su variable isSelected para poder verse
     * en la actividad del juego
     */
    private fun selectJugador(jugador: Jugador){
        val id : String = jugador.id.toString()
        val updates: HashMap<String, Any> = HashMap()
        updates["isSelected"]=true
        db.collection("users").document(id).update(updates)
    }

    /**
     * Si el jugador está desmarcado no aparecerá en la actividad del juego
     */
    private fun noSelect(jugador: Jugador){
        val id : String = jugador.id.toString()
        val updates: HashMap<String, Any> = HashMap()
        updates["isSelected"]=false
        db.collection("users").document(id).update(updates)
    }
}