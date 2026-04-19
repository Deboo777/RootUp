package com.example.rootup.view

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rootup.viewmodel.PlantViewModel

@Composable
fun MainDin ( modifier: Modifier = Modifier,viewModel: PlantViewModel = viewModel()){
    Text("Днивник")
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview())
    { result ->
        bitmap.value = result
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.size(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch(null) }) {
            Text(text = "Сделать фото")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Твой прогресс полива:")

            DayTracker(viewModel)

        }

    }
}
@Composable
fun DayTracker(viewModel: PlantViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(viewModel.totalDays) { index ->
                val isDone = index < viewModel.completedDays
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDone) Color(0xFF4CAF50) else Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${index + 1}", color = if (isDone) Color.White else Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.addProgress() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(if (viewModel.completedDays < viewModel.totalDays) "Отметить день" else "Начать новую неделю")
        }
        Button(onClick = { viewModel.resetProgress() },
            modifier = Modifier.fillMaxWidth(0.8f)
            ){
            Text("Обнуление счетчика")
        }
    }
}