package com.dam.mvvm_basic
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random // Necesario para crearRandom

class MyViewModel(): ViewModel() {

    private val TAG_LOG = "miDebug"
    // Estado del juego (observable por la IU)
    var _estadoJuego = mutableStateOf(Datos.estadoJuego)
        private set

    // Cuenta atrás (observable por la IU)
    var _cuentaAtras = mutableIntStateOf(Datos.cuentaAtras)
        private set

    // Job para cancelar la corrutina de cuenta atrás
    private var countdownJob: Job? = null


    init {
        Log.d(TAG_LOG, "Inicializamos ViewModel. Estado: INICIO")
    }


    /**
     * Inicia la secuencia de juego.
     */
    fun iniciarJuego() {
        if (_estadoJuego.value == Estados.INICIO || _estadoJuego.value == Estados.FINALIZADO) {
            actualizarEstado(Estados.GENERANDO)
            crearRandom()
            iniciarCuentaAtras()

            // LLAMADA E3: Llamamos a la función auxiliar al iniciar
            estadosAuxiliares("Mensaje Inicial: Vamos a jugar!")
        }
    }

    /**
     * Crea un número random (0-3) y lo guarda en Datos.
     */
    private fun crearRandom() {
        Datos.numero = Random.nextInt(0, 4) // Genera 0, 1, 2, o 3
        Log.d(TAG_LOG, "Random creado: ${Datos.numero}")
        actualizarEstado(Estados.ADIVINANDO) // Pasa al estado de adivinar
    }

    /**
     * Inicia la corrutina de cuenta atrás.
     */
    private fun iniciarCuentaAtras() {
        // Cancela la corrutina anterior si existe
        countdownJob?.cancel()
        // Reinicia el contador
        _cuentaAtras.intValue = Datos.cuentaAtras

        // Lanza una nueva corrutina
        countdownJob = viewModelScope.launch {
            while (_cuentaAtras.intValue > 0 && isActive) {
                delay(1000) // Espera 1 segundo
                actualizarCuentaAtras(_cuentaAtras.intValue - 1)
            }
            // Si la corrutina termina sin ser cancelada (tiempo agotado)
            if (isActive) {
                Log.d(TAG_LOG, "Tiempo agotado. Volviendo a INICIO.")
                actualizarEstado(Estados.FINALIZADO) // Muestra FIN
                delay(1500)
                actualizarEstado(Estados.INICIO) // Vuelve a INICIO
            }
        }
    }

    /**
     * Función que aplica la lambda del estado a un mensaje y lo imprime en Logcat.
     */
    fun estadosAuxiliares(msg: String = "") {
        viewModelScope.launch {
            // Recorremos los estados requeridos (GENERANDO, ADIVINANDO, CONTANDO)
            val estadosTransformadores = listOf(Estados.GENERANDO, Estados.ADIVINANDO, Estados.CONTANDO)

            for (estado in estadosTransformadores) {
                // Usamos la lambda del estado para transformar el mensaje
                val mensajeTransformado = estado.lambdaFunction(msg)

                // El Logcat imprime el mensaje transformado
                Log.d(TAG_LOG, "ESTADO AUX: ${estado}")
                Log.d(TAG_LOG, "MENSAJE (corutina): ${mensajeTransformado}")

                delay(500) // Pausa para ver los logs
            }
        }
    }

    /**
     * Comprueba si el botón pulsado es el correcto.
     */
    fun comprobar(ordinal: Int): Boolean {
        // Solo comprueba si el juego está activo
        if (_estadoJuego.value == Estados.ADIVINANDO || _estadoJuego.value == Estados.CONTANDO) {
            if (ordinal == Datos.numero) {
                Log.d(TAG_LOG, "Acierto!")
                countdownJob?.cancel() // Cierra el contador
                actualizarEstado(Estados.INICIO) // Vuelve a INICIO
                // LLAMADA E3: Al acertar
                estadosAuxiliares("GANADOR! Juego reseteado")
                return true
            } else {
                Log.d(TAG_LOG, "Error, inténtalo de nuevo")
                // LLAMADA E3: Al fallar
                estadosAuxiliares("FALLO. Intenta de nuevo")
                return false
            }
        }
        return false
    }

    // --- Funciones de actualización de estado ---
    private fun actualizarCuentaAtras(nuevoValor: Int) {
        _cuentaAtras.intValue = nuevoValor
    }

    private fun actualizarEstado(nuevoEstado: Estados) {
        Datos.estadoJuego = nuevoEstado
        _estadoJuego.value = nuevoEstado
        Log.d(TAG_LOG, "Estado cambiado a: ${nuevoEstado}")
    }
}