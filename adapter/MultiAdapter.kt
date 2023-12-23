package com.adrian.soccersala.adapter

import android.graphics.Color
import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.adrian.soccersala.viewHolder.MultiViewHolder
import com.squareup.picasso.Picasso


class MultiAdapter(private val jugadorList: ArrayList<Jugador>) :
    RecyclerView.Adapter<MultiViewHolder>() {
    //Devuelve a MultiViewHolder los atributos necesarios para pintarlos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MultiViewHolder(layoutInflater.inflate(R.layout.item_multi_list, parent, false))
    }
    //Devuelve el tamaño de la lista de jugadores
    override fun getItemCount(): Int = jugadorList.size

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        val item: Jugador = jugadorList[position]
        holder.render(item)
    }
}


