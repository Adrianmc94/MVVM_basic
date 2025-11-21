package com.dam.mvvm_basic

import androidx.compose.ui.graphics.Color

/**
 * Clase para almacenar los datos del juego
 */
object Datos {
    var numero = 0
    // Variable para el estado del juego
    var estadoJuego = Estados.INICIO
    // Variable para la cuenta atrás (tiempo restante)
    var cuentaAtras = 5
}
/**
 * Estados del juego para controlar la lógica y la UI.
 */
enum class Estados {
    INICIO,
    GENERANDO,
    ADIVINANDO,
    CONTANDO, // Estado auxiliar para mostrar el progreso de la cuenta atrás
    FINALIZADO,
    REINICIANDO
}

/**
 * Colores utilizados
 */
enum class Colores(val color: Color, val txt: String) {
    CLASE_ROJO(color = Color.Red, txt = "ROJO"),
    CLASE_VERDE(color = Color.Green, txt = "VERDE"),
    CLASE_AZUL(color = Color.Blue, txt = "AZUL"),
    CLASE_AMARILLO(color = Color.Yellow, txt = "AMARILLO"),
    CLASE_START(color = Color.LightGray, txt = "START")
}
