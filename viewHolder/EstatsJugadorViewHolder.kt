package com.adrian.soccersala.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.R

class EstatsJugadorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Referencia a los objetos del layout item_jugador
     * que se le pasaran a EstatsJugadorAdapter.kt
     * para mostrar las estadísticas de los jugadores
     */
    val golPortero = view.findViewById<TextView>(R.id.golPorterotv)
    val golJugador = view.findViewById<TextView>(R.id.golJugadorTv)
    val golesTv = view.findViewById<TextView>(R.id.golesTv)
    val pasesEdt = view.findViewById<TextView>(R.id.pasesTextV)
    val tirosEdt = view.findViewById<TextView>(R.id.tirosTextV)
    val tarjetaAmarillaTv = view.findViewById<TextView>(R.id.amarillaTextV)
    val tarjetaRojaTv = view.findViewById<TextView>(R.id.rojaTextV)
    val imagenJugador = view.findViewById<ImageView>(R.id.jugadorImV)
    val penaltiJugador = view.findViewById<TextView>(R.id.penaltiJugadorMarcadoTextV)
    val penaltiJugadorFallado = view.findViewById<TextView>(R.id.penaltiJugadorFalladoTextV)
    val penaltiPortero = view.findViewById<TextView>(R.id.penaltiPorteroTextV)
    val penaltiPorteroFallado = view.findViewById<TextView>(R.id.penaltiPorteroFalladoTV)
    val paradas = view.findViewById<TextView>(R.id.paradaTextV)
    val linearPases = view.findViewById<LinearLayoutCompat>(R.id.pasesLinL)
    val linearTiros = view.findViewById<LinearLayoutCompat>(R.id.tirosLinL)
    var linearParada = view.findViewById<LinearLayoutCompat>(R.id.paradaLinL)
    val linearPenaltiJugadorMarcado =
        view.findViewById<LinearLayoutCompat>(R.id.penaltiJugadorMarcadoLinL)
    val linearPenaltiJugadorFallado =
        view.findViewById<LinearLayoutCompat>(R.id.penaltiJugadorFalladoLinL)
    val linearPenaltiPortero = view.findViewById<LinearLayoutCompat>(R.id.penaltiPorteroLinL)
    val linearPenaltiPorteroFallado =
        view.findViewById<LinearLayoutCompat>(R.id.penaltiPorteroFalladoLinL)


}