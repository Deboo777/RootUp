package com.example.rootup.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rootup.viewmodel.PlantViewModel

@Composable
fun Detilit(
    plantId: Int,
    viewModel: PlantViewModel,
    onAdded: () -> Unit
) {
    val plants by viewModel.catalogPlants.collectAsState(initial = emptyList())
    val plant = plants.find { it.id == plantId }

    var isEditing by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }

    LaunchedEffect(plant) {
        plant?.let { if (customName.isEmpty()) customName = it.name }
    }

    Scaffold(
        bottomBar = {
            plant?.let { p ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = customName,
                            onValueChange = { customName = it },
                            label = { Text("Имя растения в вашем дневнике") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (customName.isNotBlank()) {
                                    viewModel.addCustomPlant(p, customName)
                                    onAdded()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            enabled = customName.isNotBlank()
                        ) {
                            Text("Подтвердить и сохранить")
                        }

                        TextButton(
                            onClick = { isEditing = false },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Отмена")
                        }
                    } else {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text("Добавить в мой дневник")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (plant != null) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Интервал полива:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${plant.water_interval_days} дн.", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Описание ухода:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = plant.description ?: "Нет данных",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}