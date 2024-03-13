package com.dmobley0608.cwac.ui.views.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmobley0608.cwac.R
import com.dmobley0608.cwac.data.view_model.AuthViewModel
import com.dmobley0608.cwac.ui.components.OutlinedPasswordField
import com.dmobley0608.cwac.ui.views.LoadingScreen

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }
    val context = LocalContext.current
    if (authViewModel.isLoading.value) {
        LoadingScreen()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.size(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.logo_no_bg),
                    contentDescription = "Logo"
                )
                Text(
                    text = "C W A C",
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = error.value,
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row {
                OutlinedPasswordField(password = password, passwordVisible = showPassword)
            }

            Button(
                onClick = {

                    authViewModel.signIn(email, password.value)

                    when (authViewModel.isLoading.value) {
                        true -> {
                        }

                        else -> {
                            if (authViewModel.userIsAuthenticated.value) {
                                onLoginSuccess()
                            }
                        }
                    }


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Login")

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Don't have an account? Sign up.",
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}