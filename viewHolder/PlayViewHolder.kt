package com.adrian.soccersala.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.squareup.picasso.Picasso

class PlayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Referencia a los objetos del layout item_play
     * que se le pasaran a PlayAdapter.kt
     * para mostrar a los jugadores seleccionados para jugar el partido
     */
    val nombrePlayEdt = view.findViewById<TextView>(R.id.jugadorNombrePlayTv)
    val nicknamePlayEdt = view.findViewById<TextView>(R.id.jugadorNicknamePlayTv)
    val numberPlayEdt = view.findViewById<TextView>(R.id.jugadorDorsalPlayTv)
    val imagenPlayJugador = view.findViewById<ImageView>(R.id.jugadorPlayImV)


    /**
     * Función para controlar la acción de pulsar en cada ReciclerView de
     * PlayActivity
     */
    fun render(item: Jugador, onClickListener: (Jugador) -> Unit) {
        nombrePlayEdt.text = item.nombre
        nicknamePlayEdt.text = item.nickname
        numberPlayEdt.text = item.dorsal.toString()
        Picasso.get().load(item.imagen).into(imagenPlayJugador)
        itemView.setOnClickListener {
            onClickListener(item)
        }
    }
}