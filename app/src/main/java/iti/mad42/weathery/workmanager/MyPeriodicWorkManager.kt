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
import java.util.*
import java.util.concurrent.TimeUnit

class MyPeriodicWorkManager(
    context: Context,
    workerParams: WorkerParameters,
    repository : RepositoryInterface = Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(context), context)
) :
    Worker(context, workerParams) {

    var delay : Long = 0
    var timeNow : Long = 0
    var context = context
    var repo : RepositoryInterface = repository
    var alertsList : List<AlarmPojo?>? = null
    var alertsSingleList : Single<List<AlarmPojo>>? = null

    @NonNull
    @Override
    override fun doWork(): Result {
        alertsSingleList = repo.allAlarmsList
        Log.e("sandra", "doWork: alertList ${alertsSingleList}", )
        subscribeOnSingleForAlertReminder()
        getTimePeriod()
        getCurrentAlarms()
        return Result.success()
    }

    fun subscribeOnSingleForAlertReminder(){
        alertsSingleList?.subscribe(object : SingleObserver<List<AlarmPojo?>>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: List<AlarmPojo?>) {
                alertsList = t
            }

            override fun onError(e: Throwable) {

            }

        })
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
        if(delay > 0){
            return true
        }
        return false
    }

    private fun setOnTimeWorkManger(time: Long, alarmPojo: AlarmPojo) {
        Log.e("sandra", "setOnTimeWorkManger: " + alarmPojo.alarmTitle + time)
        val data = Data.Builder()
            .putString(AlarmOneTimeWorkManager.ALARM_TAG, serializeToJason(alarmPojo))
            .build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val tag: String = alarmPojo.alarmTitle + alarmPojo.alarmTime.toString()
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(AlarmOneTimeWorkManager::class.java).setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
    }

    private fun serializeToJason(pojo: AlarmPojo): String? {
        val gson = Gson()
        return gson.toJson(pojo)
    }


    @SuppressLint("NewApi")
    fun getCurrentAlarms(){
        if(alertsList != null){
            for (alert in alertsList!!) {
                if (alert?.alarmDays?.stream()?.anyMatch { s -> s.contains(Utility.getCurrentDay()) } == true) {
                    if (checkPeriod(alert.alarmTime)) {
                        setOnTimeWorkManger(delay, alert)
                    }
                }
            }
        }
    }
}