package com.example.rootup.view.plant_edit_and_info

import android.graphics.Bitmap
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rootup.model.autho.UploadState
import com.example.rootup.viewmodel.Din_and_Offic.PlantViewModel

@Composable
fun PlantStatsScreen(
    plantId: Int,
    viewModel: PlantViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val allPlants by viewModel.allPlants.collectAsState(initial = emptyList())
    val plant = allPlants.find { it.id == plantId }

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val uploadState by viewModel.uploadState.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { takenBitmap ->
        if (takenBitmap != null) {
            bitmap.value = takenBitmap
            viewModel.uploadPlantPhoto(takenBitmap, plantId)
        }
    }

    val scrollState = rememberScrollState()
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        viewModel.updatePlantWateringDate(plantId, selectedMillis)
                    }
                    showDatePicker = false
                }) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = plant?.name ?: "Загрузка...",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (!plant?.photo_path.isNullOrEmpty()) {
                AsyncImage(
                    model = plant!!.photo_path,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (bitmap.value != null) {
                Image(
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Нет фото", color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { launcher.launch(null) },
            enabled = uploadState !is UploadState.Loading
        ) {
            if (uploadState is UploadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Загрузка...")
            } else {
                Text("Сделать фото")
            }
        }

        if (uploadState is UploadState.Error) {
            Text(
                text = "Ошибка загрузки: ${(uploadState as UploadState.Error).message}",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        plant?.let { p ->
            val daysPassed = viewModel.getDaysPassed(p.last_watered_timestamp)

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
                    Text(
                        text = "С момента полива прошло: $daysPassed дн.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (daysPassed >= (p.water_interval_days ?: 0)) Color.Red else Color.Unspecified
                    )
                }
            }

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
                    Text(text = "Нужен полив раз в:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${p.water_interval_days ?: 0} дн.", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Выбрать дату в календаре")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    plant?.let { p ->
                        viewModel.waterPlant(p, context)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Полил сегодня")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Описание ухода:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = p.description ?: "Информация отсутствует.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Фотодневник (История роста):",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            val photosList = remember(p.diary_photos_str) {
                if (!p.diary_photos_str.isNullOrEmpty()) {
                    p.diary_photos_str.split(",").filter { it.isNotBlank() }
                } else {
                    emptyList()
                }
            }

            if (photosList.isEmpty()) {
                Text(
                    text = "Вы еще не делали снимков для этого растения.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                photosList.forEachIndexed { index, url ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Снимок №${index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            AsyncImage(
                                model = url,
                                contentDescription = "Фото из архива дневника",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}




