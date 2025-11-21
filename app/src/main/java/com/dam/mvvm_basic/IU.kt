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
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Interfaz de usuario
 */
@Composable
fun IU(miViewModel: MyViewModel = viewModel()) {
    // Observamos el estado del juego para actualizar la UI
    val estadoJuego by miViewModel._estadoJuego
    val cuentaAtras by miViewModel._cuentaAtras

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Cuadro de texto para mostrar la cuenta atrás
        if (estadoJuego == Estados.CONTANDO) {
            Text(
                text = "empezó escoge!! $cuentaAtras...",
                fontSize = 30.sp,
                color = if (cuentaAtras <= 2) Colores.CLASE_ROJO.color else Colores.CLASE_AZUL.color
            )
        } else if (estadoJuego == Estados.FINALIZADO) {
            Text(text = "TIEMPO AGOTADO!!!", fontSize = 24.sp, color = Colores.CLASE_ROJO.color)
        } else {
            Text(text = "Adivina el Color", fontSize = 24.sp)
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
    // El botón solo está activo si estamos ADIVINANDO o CONTANDO
    val isEnabled = estadoActual == Estados.ADIVINANDO || estadoActual == Estados.CONTANDO


    Spacer(modifier = Modifier.size(10.dp))


    Button(
        colors =  ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            Log.d(TAG_LOG, "Botón pulsado: ${enum_color.ordinal}")
            miViewModel.comprobar(enum_color.ordinal)
        },
        enabled = isEnabled, // Control de habilitación
        modifier = Modifier
            .size((80).dp, (40).dp)
    ) {
        Text(text = enum_color.txt, fontSize = 10.sp, color = if (enum_color == Colores.CLASE_AMARILLO) Colores.CLASE_START.color else Colores.CLASE_START.color)
    }
}

@Composable
fun Boton_Start(miViewModel: MyViewModel, enum_color: Colores, estadoActual: Estados) {


    val TAG_LOG: String = "miDebug"
    // El botón Start solo está activo en INICIO
    val isEnabled = estadoActual == Estados.INICIO


    Spacer(modifier = Modifier.size(40.dp))
    Button(
        colors =  ButtonDefaults.buttonColors(enum_color.color),
        onClick = {
            Log.d(TAG_LOG, "Dentro del Start")
            miViewModel.iniciarJuego() // Llama a la nueva función de inicio
        },
        enabled = isEnabled, // Control de habilitación
        modifier = Modifier
            .size((100).dp, (40).dp)
    ) {
        val buttonText = when (estadoActual) {
            Estados.FINALIZADO -> "REINTENTAR"
            Estados.GENERANDO -> "..."
            Estados.ADIVINANDO, Estados.CONTANDO -> "JUGANDO"
            else -> "START"
        }
        Text(text = buttonText, fontSize = 10.sp, color = Colores.CLASE_ROJO.color)
    }
}
@Preview(showBackground = true)
@Composable
fun IUPreview() {
    IU(MyViewModel())
}
