package com.adrian.soccersala.model

import java.util.Date

data class Jugador(

    /**
     * Data class con las carácteristicas de cada Jugador
     */
    var equipo: String? =null,
    var id: String? = null,
    var isSelected: Boolean? = null,
    val seeIt : Boolean? = null,
    var nombre: String? = null,
    var nickname: String? = null,
    var dorsal: String? = null,
    var lastName: String? = null,
    var correo: String? = null,
    var fechaNac: Date? = null,
    var posicion: String? = null,
    var imagen: String? = null,
    var gol: Int? =null,
    var pases: Int? =null,
    var tiros: Int? =null,
    var out :Int? = null,
    var parada : Int? =null,
    var tarjetaAmarilla : Int? =null,
    var tarjetaRoja: Int? =null,
    var penaltiJugadorMarcado: Int? =null,
    var penaltiJugadorFallado: Int? =null,
    var penaltiPortero : Int? =null,
    var penaltiPorteroFallado: Int? =null
)