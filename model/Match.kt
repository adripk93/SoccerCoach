package com.adrian.soccersala.model

import java.util.Date


/**
 * Data class con las carácteristicas de Encuentro
 */
data class Match(
    var equipo: String? = null,
    var equipo_rival: String? = null,
    var jornada: Int? = null,
    var estadio: String? = null,
    var fecha_encuentro: Date? = null
)