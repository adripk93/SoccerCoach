package com.adrian.soccersala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrian.soccersala.MenuActivity
import com.adrian.soccersala.model.Jugador
import com.adrian.soccersala.R
import com.adrian.soccersala.viewHolder.EditViewHolder
import com.squareup.picasso.Picasso

class EditAdpter(
    private val jugadorList: ArrayList<Jugador>,
    private val itemClickListener: OnButtonClickListener,
    private val email: String
) :
    RecyclerView.Adapter<EditViewHolder>() {

    interface OnButtonClickListener {
        fun editBtt(id: String?, email: String)
        fun deleteBtt(id: String?, email: String)
    }

    //Devuelve a JugadorViewHolder los atributos necesarios para pintarlos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EditViewHolder(layoutInflater.inflate(R.layout.item_edit, parent, false))
    }

    //Devuelve el tamaño de la lista de jugadores
    override fun getItemCount(): Int = jugadorList.size


    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        val item: Jugador = jugadorList[position]
        holder.nombreEditEdt.text = item.nombre
        holder.nicknameEditEdt.text = item.nickname
        holder.numberEditEdt.text = item.dorsal.toString()
        Picasso.get().load(item.imagen).into(holder.imagenEditJugador)
        holder.editButton.setOnClickListener {
            itemClickListener.editBtt(item.id, email)
        }
        holder.deleteButton.setOnClickListener {
            itemClickListener.deleteBtt(item.id, email)
        }
    }
}