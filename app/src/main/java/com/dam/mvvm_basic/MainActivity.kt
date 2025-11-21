package com.dam.mvvm_basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Importaciones clave para el Singleton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.mvvm_basic.ui.theme.MVVM_basicTheme

// 1. DEFINICIÓN DEL SINGLETON
// Definimos la factoría como una variable global (val) fuera de la Activity.
// Esto garantiza que el constructor de MyViewModel() se llame UNA SOLA VEZ
val SingletonFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        MyViewModel() // La única vez que se llama al constructor
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ELIMINAMOS la inicialización directa que crea una nueva instancia en cada onCreate:
        // val miViewModel: MyViewModel = MyViewModel()

        enableEdgeToEdge()
        setContent {
            MVVM_basicTheme {
                // 2. LLAMAMOS a la IU, pasándole la Factoría Singleton.
                // Compose se encargará de obtener la instancia única.
                IU(factory = SingletonFactory)
            }
        }
    }
}