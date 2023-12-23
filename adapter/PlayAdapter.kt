package com.adrian.soccersala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.adrian.soccersala.viewHolder.PlayViewHolder

class PlayAdapter(
    private val jugadorList: ArrayList<Jugador>,
    private val onClickListener: (Jugador) -> Unit
) :
    RecyclerView.Adapter<PlayViewHolder>() {
    //Devuelve a PlayViewHolder los atributos necesarios para pintarlos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlayViewHolder(layoutInflater.inflate(R.layout.item_play, parent, false))
    }

    //Devuelve el tamaño de la lista de jugadores
    override fun getItemCount(): Int = jugadorList.size

    override fun onBindViewHolder(holder: PlayViewHolder, position: Int) {
        val item: Jugador = jugadorList[position]
        holder.render(item, onClickListener)
    }
}