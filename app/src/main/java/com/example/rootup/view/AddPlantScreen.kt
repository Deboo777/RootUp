package com.example.rootup.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rootup.viewmodel.PlantViewModel

@Composable
fun AddPlantScreen(viewModel: PlantViewModel, onSave: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Новый житель дневника", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Имя растения") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = interval,
            onValueChange = { if (it.all { c -> c.isDigit() }) interval = it },
            label = { Text("Интервал полива (в днях)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Описание") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                viewModel.insertNewTypeToCatalog(name, interval, desc)
                onSave()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && interval.isNotBlank()
        ) {
            Text("Сохранить")
        }
    }
}