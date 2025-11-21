package com.dam.mvvm_basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.mvvm_basic.ui.theme.MVVM_basicTheme

// Definimos la factoría como un Singleton (val) fuera de la Activity
// Esto garantiza que el ViewModel creado sea el mismo para toda la app
val SingletonFactory: ViewModelProvider.Factory = viewModelFactory {
    // El initializer define cómo se crea la única instancia del ViewModel
    initializer {
        MyViewModel()
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVM_basicTheme {
                // 2. Llamamos a la IU pasando la SingletonFactory
                IU(factory = SingletonFactory)
            }
        }
    }
}