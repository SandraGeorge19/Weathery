package iti.mad42.weathery.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.*
import iti.mad42.weathery.workmanager.alarmreminder.AlarmOneTimeWorkManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MyPeriodicWorkManager(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    var delay : Long = 0
    var timeNow : Long = 0
    var context = context
    var repo : RepositoryInterface = Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(context), context)
    var alertsList : List<AlarmPojo?> = listOf()

    @NonNull
    @Override
    override suspend fun doWork(): Result {
        repo.allAlarmsList
        subscribeOnSingleForAlertReminder()
        getTimePeriod()
        getCurrentAlarms()
        return Result.success()
    }

    @DelicateCoroutinesApi
    fun subscribeOnSingleForAlertReminder(){

        GlobalScope.launch(Dispatchers.IO) {
            repo.allAlarmsList?.subscribe { alerts->
                alertsList =alerts
            }
        }
    }

    fun getTimePeriod(){
        var calender : Calendar = Calendar.getInstance()
        var hour : Int = calender.get(Calendar.HOUR_OF_DAY)
        var minute = calender.get(Calendar.MINUTE)

        timeNow = (hour*60).toLong()
        timeNow = (timeNow + minute) * 60 * 1000
    }

    fun checkPeriod(alarmTime : Long) : Boolean{
        delay = alarmTime - timeNow
        Log.e("sandra", "getCurrentAlarms: delay : $delay", )
        if(delay > 0){
            return true
        }
        return false
    }

    private fun setOnTimeWorkManger(time: Long, desc : String) {
        val data = Data.Builder()
            .putString(AlarmOneTimeWorkManager.ALARM_TAG, desc)
            .build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val tag: String = desc + ""
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(AlarmOneTimeWorkManager::class.java).setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build()
        WorkManager.getInstance()
            .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
    }

    @SuppressLint("NewApi")
     suspend fun getCurrentAlarms(){
        var currentWeather : WeatherPojo = repo.getStoredWeather()
        if(alertsList != null){
            for (alert in alertsList!!) {
                if (alert?.alarmDays?.stream()?.anyMatch { s -> s.contains(Utility.getCurrentDay()) } == true) {
                    if (checkPeriod(alert.alarmTime)) {
                        //setOnTimeWorkManger(delay, alert)
                        if(currentWeather.alerts.isNullOrEmpty()){
                            Log.e("sandra", "getCurrentAlarms: is", )
                            setOnTimeWorkManger(delay, currentWeather.current.weather[0].description)

                        }else{
                            setOnTimeWorkManger(delay, currentWeather.alerts!![0].description)
                        }
                    }
                }
            }
        }
    }
}