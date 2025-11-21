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
    // Si se pasa un Factory (el Singleton), Compose lo usa para obtener la instancia
    val miViewModel: MyViewModel = if (factory != null) {
        viewModel(factory = factory)
    } else {
        viewModel()
    }

    // Observamos el estado del juego (Estados) para actualizar la UI (Patrón Observer)
    val estadoJuego by miViewModel._estadoJuego
    // Observamos el valor de la cuenta atrás (tiempo) para actualizar el texto
    val cuentaAtras by miViewModel._cuentaAtras


    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        // Cuadro de texto para mostrar el estado y la cuenta atrás
        if (estadoJuego == Estados.CONTANDO) {
            // Muestra la cuenta atrás. Si quedan 2 segundos o menos, el color cambia a rojo.
            Text(
                text = "empezó escoge!! $cuentaAtras...",
                fontSize = 30.sp,
                color = if (cuentaAtras <= 2) Colores.CLASE_ROJO.color else Colores.CLASE_AZUL.color
            )
        } else if (estadoJuego == Estados.FINALIZADO) {
            // Se muestra si la corrutina de cuenta atrás terminó sin ser cancelada (tiempo agotado)
            Text(text = "TIEMPO AGOTADO!!!", fontSize = 24.sp, color = Colores.CLASE_ROJO.color)
        } else if (estadoJuego == Estados.INICIO) {
            Text(text = "Pulsa START", fontSize = 24.sp)
        } else if (estadoJuego == Estados.GENERANDO) {
            Text(text = "GENERANDO...", fontSize = 24.sp)
        }


        Spacer(modifier = Modifier.size(20.dp))


        // Botones de colores
        Boton(miViewModel, Colores.CLASE_ROJO, estadoJuego)
        Boton(miViewModel, Colores.CLASE_VERDE, estadoJuego)
        Boton(miViewModel, Colores.CLASE_AZUL, estadoJuego)
        Boton(miViewModel, Colores.CLASE_AMARILLO, estadoJuego)


        // Botón Start
        Boton_Start(miViewModel, Colores.CLASE_START, estadoJuego)
    }
}
@Composable
fun Boton(miViewModel: MyViewModel, enum_color: Colores, estadoActual: Estados) {
    val TAG_LOG: String = "miDebug"
    // El botón solo está activo si el juego está en modo ADIVINANDO o CONTANDO
    val isEnabled = estadoActual == Estados.ADIVINANDO || estadoActual == Estados.CONTANDO


    Spacer(modifier = Modifier.size(20.dp))
    Button(
        colors = ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            // Llama a la lógica del ViewModel para comprobar el acierto/fallo
            Log.d(TAG_LOG, "Boton pulsado: ${enum_color.ordinal}")
            miViewModel.comprobar(enum_color.ordinal)
        },
        enabled = isEnabled, // Control de habilitación basado en el estado
        modifier = Modifier
            .size((80).dp, (40).dp)
    ) {
        Text(text = enum_color.txt, fontSize = 10.sp, color = if (enum_color == Colores.CLASE_AMARILLO) Colores.CLASE_START.color else Colores.CLASE_START.color)
    }
}
@Composable
fun Boton_Start(miViewModel: MyViewModel, enum_color: Colores, estadoActual: Estados) {
    val TAG_LOG: String = "miDebug"
    // El botón Start solo está activo en INICIO o FINALIZADO (para reintentar)
    val isEnabled = estadoActual == Estados.INICIO || estadoActual == Estados.FINALIZADO


    Spacer(modifier = Modifier.size(40.dp))
    Button(
        colors = ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            Log.d(TAG_LOG, "Dentro del Start")
            miViewModel.iniciarJuego() // Llama a la función que inicia el random y la cuenta atrás (Corrutina)
        },
        enabled = isEnabled, // Control de habilitación basado en el estado
        modifier = Modifier
            .size((100).dp, (40).dp)
    ) {
        // Cambia el texto del botón si el juego ha finalizado
        val buttonText = when (estadoActual) {
            Estados.FINALIZADO -> "REINTENTAR"
            else -> "START"
        }
        Text(text = buttonText, fontSize = 10.sp, color = Colores.CLASE_AZUL.color)
    }
}
@Preview(showBackground = true)
@Composable
fun IUPreview() {
    IU()
}