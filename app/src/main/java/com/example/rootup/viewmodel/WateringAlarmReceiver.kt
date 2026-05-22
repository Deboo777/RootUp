package com.example.rootup.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class WateringAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val plantId = intent.getIntExtra("PLANT_ID", 0)
        val plantName = intent.getStringExtra("PLANT_NAME") ?: "Растение"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "watering_reminders",
                "Полив растений",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Напоминания о необходимости полива цветов"
            }
            manager.createNotificationChannel(channel)
        }


        val builder = NotificationCompat.Builder(context, "watering_reminders")
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle("Пора полить!")
            .setContentText("Растение \"$plantName\" хочет пить.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(plantId, builder.build())
    }
}