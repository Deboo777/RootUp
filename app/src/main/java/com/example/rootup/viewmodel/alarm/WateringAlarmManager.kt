package com.example.rootup.viewmodel.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.rootup.model.data_plant.Plant
import com.example.rootup.viewmodel.alarm.WateringAlarmReceiver

object WateringAlarmManager {

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleWateringAlarm(context: Context, plant: Plant) {
        val plantId = plant.id ?: return

        val lastWatered = plant.last_watered_timestamp ?: System.currentTimeMillis()
        val intervalDays = plant.water_interval_days ?: 3

        val triggerTime = lastWatered + (intervalDays * 86400000L)

        val intent = Intent(context, WateringAlarmReceiver::class.java).apply {
            putExtra("PLANT_ID", plantId)
            putExtra("PLANT_NAME", plant.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            plantId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
}