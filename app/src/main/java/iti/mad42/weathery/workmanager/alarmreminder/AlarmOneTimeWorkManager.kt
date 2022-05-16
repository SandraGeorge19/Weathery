package iti.mad42.weathery.workmanager.alarmreminder

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmOneTimeWorkManager(
    context: Context,
    workerParams: WorkerParameters
) :
    Worker(context, workerParams) {

    lateinit var returnedData : Data
    companion object{
        var ALARM_TAG = "alarmTag"
    }

    override fun doWork(): Result {
        returnedData = inputData
        startMyService()
        return Result.success()
    }

    private fun startMyService(){
        var intent : Intent = Intent(applicationContext, AlarmReminderService::class.java)
        intent.putExtra(ALARM_TAG, returnedData.getString(ALARM_TAG))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }
}