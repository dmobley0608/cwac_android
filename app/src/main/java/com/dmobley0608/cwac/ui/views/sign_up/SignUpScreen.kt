package com.dmobley0608.cwac.ui.views.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmobley0608.cwac.data.view_model.AuthViewModel
import com.dmobley0608.cwac.ui.components.OutlinedPasswordField
import com.dmobley0608.cwac.ui.views.LoadingScreen


@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val firstname = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val showConfirmPassword = remember{ mutableStateOf(false) }
    val passwordError = remember { mutableStateOf("") }
    val showPassword = remember{ mutableStateOf(false) }


    if(authViewModel.isLoading.value) {
        LoadingScreen()
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Register", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.heightIn(20.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstname.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { firstname.value = it },
                label = { Text(text = "First Name") },

                )
            Spacer(modifier = Modifier.heightIn(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { lastName.value = it },
                label = { Text(text = "Last Name") }
            )
            Spacer(modifier = Modifier.heightIn(20.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )

            )
            Spacer(modifier = Modifier.heightIn(20.dp))
            Text(text = passwordError.value, color=Color.Red)
            OutlinedPasswordField(password, showPassword)
            OutlinedPasswordField(confirmPassword, showConfirmPassword)

            Spacer(modifier = Modifier.heightIn(20.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if(password.value != confirmPassword.value){
                        passwordError.value = "Passwords Must Match"
                    }else {
                        authViewModel.signUp(
                            email = email.value,
                            password = password.value,
                            firstName = firstname.value,
                            lastName = lastName.value,
                            role = "Employee"
                        )
                        onNavigateToLogin()
                    }
                }) {
                Text(text = "Register")
            }
            Text(
                modifier = Modifier
                    .clickable {

                        onNavigateToLogin()
                    }
                    .padding(16.dp),
                text = "Already Have An Account?",
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}