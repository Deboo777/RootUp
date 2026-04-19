package com.example.rootup.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rootup.viewmodel.Office

@Composable
fun OfficeScreen(viewModel: Office = viewModel()) {

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Photo",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = state.userName.ifEmpty { "Загрузка..." },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )


        Text(
            text = "Растений в коллекции: ${state.plantsCount}",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        Text(
            text = "Моя оранжерея",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.plants) { plant ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(plant) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    )
                }
            }
        }
    }
}