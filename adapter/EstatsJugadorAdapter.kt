package com.adrian.soccersala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.adrian.soccersala.viewHolder.EstatsJugadorViewHolder
import com.squareup.picasso.Picasso

class EstatsJugadorAdapter(private val jugadorList: ArrayList<Jugador>) :
    RecyclerView.Adapter<EstatsJugadorViewHolder>() {

    //Devuelve a EstatsViewHolder los atributos necesarios para pintarlos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstatsJugadorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EstatsJugadorViewHolder(layoutInflater.inflate(R.layout.item_jugaor, parent, false))
    }

    //Devuelve el tamaño de la lista de jugadores
    override fun getItemCount(): Int = jugadorList.size


    override fun onBindViewHolder(holder: EstatsJugadorViewHolder, position: Int) {
        val item: Jugador = jugadorList[position]
        if (item.posicion == "jugador") {
            holder.golPortero.isVisible = false
            holder.linearParada.isVisible = false
            holder.linearPenaltiPortero.isVisible = false
            holder.linearPenaltiPorteroFallado.isVisible = false
            holder.golesTv.text = item.gol.toString()
            holder.pasesEdt.text = item.pases.toString()
            holder.tirosEdt.text = item.tiros.toString()
            holder.penaltiJugador.text = item.penaltiJugadorMarcado.toString()
            holder.penaltiJugadorFallado.text = item.penaltiJugadorFallado.toString()

        } else {
            holder.golJugador.isVisible = false
            holder.linearPases.isVisible = false
            holder.linearTiros.isVisible = false
            holder.linearPenaltiJugadorMarcado.isVisible = false
            holder.linearPenaltiJugadorFallado.isVisible = false
            holder.golesTv.text = item.out.toString()
            holder.paradas.text = item.parada.toString()
            holder.penaltiPortero.text = item.penaltiPortero.toString()
            holder.penaltiPorteroFallado.text = item.penaltiPorteroFallado.toString()
        }
        holder.tarjetaAmarillaTv.text = item.tarjetaAmarilla.toString()
        holder.tarjetaRojaTv.text = item.tarjetaRoja.toString()

        Picasso.get().load(item.imagen).into(holder.imagenJugador)

    }
}