package iti.mad42.weathery.workmanager.alarmreminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import iti.mad42.weathery.R
import iti.mad42.weathery.model.pojo.AlarmPojo

class AlarmReminderService : Service() {

    lateinit var alarm : AlarmPojo

    companion object{
        var CHANNEL_ID = 30
        var FOREGROUND_ID = 40
    }

    lateinit var notificationManager: NotificationManager
    lateinit var description: String
    lateinit var reminderDialog: AlarmWindow
    private lateinit var notificationManagerCompat: NotificationManagerCompat

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        alarm = fromStringPojo(intent?.getStringExtra(AlarmOneTimeWorkManager.ALARM_TAG))!!
        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification())
        if (Settings.canDrawOverlays(this)) {
            reminderDialog = AlarmWindow(applicationContext, alarm)
            reminderDialog.setAlarmWindowManager()
        }
        return START_NOT_STICKY
    }

    fun fromStringPojo(pojoString: String?): AlarmPojo? {
        val gson = Gson()
        return gson.fromJson(pojoString, AlarmPojo::class.java)
    }

    private fun makeNotification(): Notification? {
        notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        description = "There is ${alarm.alarmType} for ${alarm.alarmTitle} Today"
        val notification: Notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID.toString()
        )
            .setSmallIcon(R.drawable.add_fav)
            .setContentText("Schedule for " + alarm.alarmTitle + ", today")
            .setContentTitle("Weather Alert Reminder") //.setLargeIcon(bitmap)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .build()
        notificationManagerCompat.notify(1, notification)
        return notification
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MedReminderChannel"
            val description = "MedReminderChannelDescription"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID.toString(),
                name, importance
            )
            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}