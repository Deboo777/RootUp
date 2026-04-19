package com.example.rootup.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rootup.viewmodel.Registration_Login

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    vm: Registration_Login = viewModel()
) {

    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Авторизация", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = vm::onPasswordChange,
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading
        )

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { vm.login(onLoginSuccess) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Войти")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onRegisterClick,
            enabled = !state.isLoading
        ) {
            Text("Регистрация")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(horizontal = 32.dp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { vm.loginAsGuest(onLoginSuccess) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти как гость")
        }
    }
}