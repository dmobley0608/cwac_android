package com.dmobley0608.cwac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dmobley0608.cwac.data.view_model.AuthViewModel
import com.dmobley0608.cwac.data.view_model.MainViewModel
import com.dmobley0608.cwac.ui.theme.CWACTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel:AuthViewModel = viewModel()
           val mainViewModel:MainViewModel = viewModel()
            CWACTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CWACApp(
                        mainViewModel=mainViewModel,
                        authViewModel=authViewModel,
                        navController=navController)
                }
            }
        }
    }
}

