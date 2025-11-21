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
 * Añadimos una función lambda para transformar mensajes.
 * La lambda recibe String y devuelve String.
 */
enum class Estados(
    // La lambda: recibe un String (it) y devuelve un String
    val lambdaFunction: (String) -> String = { it } // Valor por defecto: no modifica
) {
    // AUX1 Devuelve la string sin modificar
    INICIO(),
    GENERANDO(lambdaFunction = { it }),
    // AUX2 Devuelve la string en minúsculas
    ADIVINANDO(lambdaFunction = { it.lowercase() }),
    // AUX3 Devuelve la string en mayúsculas
    CONTANDO(lambdaFunction = { it.uppercase() }),
    FINALIZADO(),
    REINICIANDO()
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