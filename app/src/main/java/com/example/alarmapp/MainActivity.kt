package com.example.alarmapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Notification Channel তৈরি করো
        createNotificationChannel()

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val btnSet    = findViewById<Button>(R.id.btnSetAlarm)
        val btnCancel = findViewById<Button>(R.id.btnCancelAlarm)
        val tvStatus  = findViewById<TextView>(R.id.tvStatus)

        timePicker.setIs24HourView(false)

        // এলার্ম সেট করুন
        btnSet.setOnClickListener {
            val hour   = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                // যদি সময় পার হয়ে গেছে তাহলে পরের দিন সেট করো
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            val amPm = if (hour < 12) "AM" else "PM"
            val h = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            tvStatus.text = "✅ এলার্ম সেট হয়েছে: $h:${minute.toString().padStart(2, '0')} $amPm"
        }

        // এলার্ম বাতিল করুন
        btnCancel.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            tvStatus.text = "❌ এলার্ম বাতিল করা হয়েছে"
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "এলার্ম নোটিফিকেশন",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "এলার্ম বাজলে এই channel থেকে notification আসবে"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
