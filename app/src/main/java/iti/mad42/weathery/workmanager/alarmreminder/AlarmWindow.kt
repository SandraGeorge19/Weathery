package iti.mad42.weathery.workmanager.alarmreminder

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.work.*
import com.google.gson.Gson
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import iti.mad42.weathery.R
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.RepositoryInterface
import iti.mad42.weathery.workmanager.MyPeriodicWorkManager
import java.lang.Exception
import java.util.concurrent.TimeUnit

class AlarmWindow(
    var context: Context,
    var alarm : AlarmPojo,
    var myDialog : Dialog = Dialog(context),
    var repository : RepositoryInterface = Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(context), context)
) {

    private lateinit var mView: View
    private lateinit var mParams: WindowManager.LayoutParams
    private lateinit var mWindowManager: WindowManager
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var alertCloseBtn: ImageView
    private lateinit var alertTitle: TextView
    private lateinit var alertSubTitle: TextView
    private lateinit var dismissBtn: Button
    lateinit var mMediaPlayer: MediaPlayer

    fun setMediaPlayer(id : Int){
        mMediaPlayer = MediaPlayer()
        mMediaPlayer = MediaPlayer.create(context, id)
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.start()
    }

    fun setAlarmWindowManager(){
        setMediaPlayer(R.raw.noti)
        repository.getSpecificAlarm(alarm.id)?.subscribe(object : SingleObserver<AlarmPojo>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(alarmPojo: AlarmPojo) {
                alarm = alarmPojo
            }

            override fun onError(e: Throwable) {

            }

        })
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.alert_dialog, null)
        initView(mView)
        var layoutFlag = 0
        layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            android.R.attr.showWhenLocked or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        mWindowManager.addView(mView, mParams)
    }

    fun initView(view : View){
        alertCloseBtn = view.findViewById(R.id.dialogCloseBtn)
        alertTitle = view.findViewById(R.id.dialogTitle)
        alertSubTitle = view.findViewById(R.id.dialogSubTitle)
        dismissBtn = view.findViewById(R.id.dismissAlertBtn)
        handleDialogBtns()
    }

    fun handleDialogBtns(){
        alertCloseBtn.setOnClickListener{
            stopMyService()
            close()
        }
        dismissBtn.setOnClickListener {
            stopMyService()
            close()
        }
    }

    fun setAlarmOneTimeWorkManager(alarmPojo: AlarmPojo){
        val data = Data.Builder()
            .putString(AlarmOneTimeWorkManager.ALARM_TAG, serializeToJason(alarmPojo))
            .build()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val tag: String = alarmPojo.alarmTitle + alarmPojo.id
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlarmOneTimeWorkManager::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .addTag(tag)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)

    }

    private fun serializeToJason(pojo: AlarmPojo): String? {
        val gson = Gson()
        return gson.toJson(pojo)
    }

    private fun setPeriodicWorkManger() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyPeriodicWorkManager::class.java,
            3, TimeUnit.HOURS
        )
            .setInitialDelay(2, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "Counter",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
            mView.invalidate()
            (mView.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("sandra", e.toString())
        }
    }

    private fun stopMyService() {
        context.stopService(Intent(context, AlarmReminderService::class.java))
    }
}