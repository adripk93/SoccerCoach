package com.adrian.soccersala.viewHolder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.squareup.picasso.Picasso

class EditViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Referencia a los objetos del layout item_edit
     * que se le pasaran a EditAdapter.kt
     * para mostrar las vistas de los jugadores para poder editarlos
     */
    val nombreEditEdt = view.findViewById<TextView>(R.id.jugadorNombreEditTv)
    val nicknameEditEdt = view.findViewById<TextView>(R.id.jugadorNicknameEditTv)
    val numberEditEdt = view.findViewById<TextView>(R.id.jugadorDorsalEditTv)
    val imagenEditJugador = view.findViewById<ImageView>(R.id.jugadorEditImV)
    val deleteButton = view.findViewById<AppCompatImageButton>(R.id.deleteBtt)
    val editButton = view.findViewById<AppCompatImageButton>(R.id.editBtt)

}