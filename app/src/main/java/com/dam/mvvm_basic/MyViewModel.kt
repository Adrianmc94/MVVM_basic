package com.dam.mvvm_basic
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {

    private val TAG_LOG = "miDebug"
    // Estado principal del juego que observa la UI
    var _estadoJuego = mutableStateOf(Datos.estadoJuego)
        private set // Solo el ViewModel puede modificarlo


    // Cuenta atrás que observa la UI
    var _cuentaAtras = mutableIntStateOf(Datos.cuentaAtras)
        private set

    // Job para poder cancelar la cuenta atrás si el usuario acierta.
    private var countdownJob: Job? = null

    init {
        Log.d(TAG_LOG, "Inicializamos ViewModel. Estado: INICIO")
    }

    /**
     * Inicia la secuencia de juego: generar random e iniciar cuenta atrás.
     */
    fun iniciarJuego() {
        if (_estadoJuego.value == Estados.INICIO || _estadoJuego.value == Estados.FINALIZADO) {
            actualizarEstado(Estados.GENERANDO)
            crearRandom()
            iniciarCuentaAtras()
        }
    }

    /**
     * Crear entero random y actualizar estado.
     */
    private fun crearRandom() {
        Datos.numero = (0..3).random()
        Log.d(TAG_LOG, "Random generado: ${Datos.numero}")
        actualizarEstado(Estados.ADIVINANDO)
    }

    /**
     * Lógica de cuenta atrás usando Corrutinas.
     * Si llega a 1, finaliza el juego (pasa a INICIO).
     */
    private fun iniciarCuentaAtras() {
        actualizarCuentaAtras(5) // Reset a 5 al iniciar
        actualizarEstado(Estados.CONTANDO)

        // Cancelar el Job anterior si existe
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            // Repite mientras la cuenta sea mayor que 0
            while (_cuentaAtras.intValue > 0) {
                delay(1000) // Espera 1 segundo
                _cuentaAtras.intValue -= 1
                Log.d(TAG_LOG, "Cuenta atrás: ${_cuentaAtras.intValue}")
            }
            // Si el bucle termina porque el tiempo se agotó (y no fue cancelado)
            if (isActive) { // Verifica que no haya sido cancelado por un acierto
                Log.d(TAG_LOG, "Tiempo agotado. Volviendo a INICIO.")
                actualizarEstado(Estados.FINALIZADO) // Estado FINALIZADO
                delay(1500) // Pequeño delay para mostrar FINALIZADO
                actualizarEstado(Estados.INICIO) // Vuelve al estado INICIO
            }
        }
    }

    /**
     * Comprobar si el botón pulsado es el correcto.
     * @param ordinal: Int número de botón pulsado
     * @return Boolean si coincide TRUE, si no FALSE
     */
    fun comprobar(ordinal: Int): Boolean {
        // Solo comprueba si el juego está activo (ADIVINANDO o CONTANDO)
        if (_estadoJuego.value == Estados.ADIVINANDO || _estadoJuego.value == Estados.CONTANDO) {
            if (ordinal == Datos.numero) {
                Log.d(TAG_LOG, "Acierto!")
                countdownJob?.cancel() // Cierra la corrutina de cuenta atrás
                actualizarEstado(Estados.INICIO) // Vuelve a INICIO
                return true
            } else {
                Log.d(TAG_LOG, "Error, inténtalo de nuevo")
                return false
            }
        }
        return false
    }
    private fun actualizarEstado(nuevoEstado: Estados) {
        Datos.estadoJuego = nuevoEstado
        _estadoJuego.value = nuevoEstado
        Log.d(TAG_LOG, "Estado cambiado a: ${nuevoEstado}")
    }
    private fun actualizarCuentaAtras(nuevoValor: Int) {
        Datos.cuentaAtras = nuevoValor
        _cuentaAtras.intValue = nuevoValor
    }
}
