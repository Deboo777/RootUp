package com.example.rootup.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants_table")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val name: String,
    val description: String?,
    val photo_path: String?,
    val water_interval_days: Int?,
    var last_watered_timestamp: Long? = System.currentTimeMillis(),
    val isAddedToDiary: Int? = 0
)