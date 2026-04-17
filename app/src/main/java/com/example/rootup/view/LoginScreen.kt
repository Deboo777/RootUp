package com.example.rootup.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rootup.viewmodel.PlantViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    vm: PlantViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Авторизация", fontSize = 28.sp, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        vm.errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { vm.login(email, password, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoading
        ) {
            if (vm.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Войти")
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { vm.loginAsGuest(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти как гость")
        }

        TextButton(onClick = onRegisterClick) {
            Text("Ещё нет аккаунта? Регистрация")
        }
    }
}
