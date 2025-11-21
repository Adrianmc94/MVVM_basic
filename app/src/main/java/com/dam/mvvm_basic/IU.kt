package com.dam.mvvm_basic

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Interfaz de usuario (Vista en MVVM)
 */
@Composable
// Modificamos la firma para aceptar el Factory
fun IU(factory: ViewModelProvider.Factory? = null) {

    // Obtenemos la instancia del ViewModel, usando la factoría si se proporcionó
    val miViewModel: MyViewModel = if (factory != null) {
        viewModel(factory = factory)
    } else {
        viewModel() // Usa el modo por defecto para Preview
    }

    val estadoJuego by miViewModel._estadoJuego
    val cuentaAtras by miViewModel._cuentaAtras


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // --- CUADRO DE TEXTO DE ESTADO/CUENTA ATRÁS ---
        if (estadoJuego == Estados.CONTANDO) {
            // Muestra la cuenta atrás. Si quedan 2 segundos o menos, el color cambia a rojo.
            Text(
                text = "empezó escoge!! $cuentaAtras...",
                fontSize = 30.sp,
                color = if (cuentaAtras <= 2) Colores.CLASE_ROJO.color else Colores.CLASE_AZUL.color
            )
        } else if (estadoJuego == Estados.FINALIZADO) {
            Text(
                text = "TIEMPO AGOTADO",
                fontSize = 30.sp,
                color = Colores.CLASE_ROJO.color
            )
        } else {
            // Muestra el estado actual (INICIO, GENERANDO, ADIVINANDO, REINICIANDO)
            Text(
                text = estadoJuego.name,
                fontSize = 30.sp,
                color = Colores.CLASE_START.color
            )
        }

        Spacer(modifier = Modifier.size(20.dp)) // Espacio entre el texto y los botones

        // --- BOTONES DE COLORES ---
        // Pasamos el estado actual para habilitar/deshabilitar los botones.
        Boton(miViewModel, Colores.CLASE_ROJO, estadoJuego)
        Boton(miViewModel, Colores.CLASE_VERDE, estadoJuego)
        Boton(miViewModel, Colores.CLASE_AZUL, estadoJuego)
        Boton(miViewModel, Colores.CLASE_AMARILLO, estadoJuego)

        // --- BOTÓN START ---
        Boton_Start(miViewModel, Colores.CLASE_START, estadoJuego)
    }
}

@Composable
fun Boton(miViewModel: MyViewModel, enum_color: Colores, estadoActual: Estados) {

    val TAG_LOG: String = "miDebug"
    // Los botones de colores solo están activos en ADIVINANDO o CONTANDO
    val isEnabled = estadoActual == Estados.ADIVINANDO || estadoActual == Estados.CONTANDO

    // separador entre botones
    Spacer(modifier = Modifier.size(10.dp))

    Button(
        // utilizamos el color del enum
        colors = ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            Log.d(TAG_LOG, "Dentro del boton: ${enum_color.ordinal}")
            miViewModel.comprobar(enum_color.ordinal)
        },
        enabled = isEnabled, // Control de habilitación basado en el estado
        modifier = Modifier
            .size((80).dp, (40).dp)
    ) {
        // utilizamos el texto del enum
        Text(text = enum_color.txt, fontSize = 10.sp, color = Colores.CLASE_START.color)
    }
}

@Composable
fun Boton_Start(miViewModel: MyViewModel, enum_color: Colores, estadoActual: Estados) {

    val TAG_LOG: String = "miDebug"
    // El botón Start solo está activo en INICIO o FINALIZADO (para reintentar)
    val isEnabled = estadoActual == Estados.INICIO || estadoActual == Estados.FINALIZADO

    // separador entre botones
    Spacer(modifier = Modifier.size(40.dp))
    Button(
        // utilizamos el color del enum
        colors = ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            Log.d(TAG_LOG, "Dentro del Start")
            miViewModel.iniciarJuego() // Llama a la función que inicia el random y la cuenta atrás
        },
        enabled = isEnabled, // Control de habilitación basado en el estado
        modifier = Modifier
            .size((100).dp, (40).dp)
    ) {
        // Cambia el texto del botón si el juego ha finalizado
        val buttonText = when (estadoActual) {
            Estados.FINALIZADO -> "REINTENTAR"
            else -> enum_color.txt // START
        }
        // utilizamos el texto del enum o REINTENTAR
        Text(text = buttonText, fontSize = 10.sp)
    }
}
/**
 * Preview de la interfaz de usuario
 */
@Preview(showBackground = true)
@Composable
fun IUPreview() {
    // Llama a IU sin Factory. Usa el viewModel() por defecto
    IU()
}