package iti.mad42.weathery.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import iti.mad42.weathery.model.pojo.NetworkUtility

class NetworkChangeReceiver : BroadcastReceiver() {

    companion object{
        var isOnline : Boolean = false
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        var status = NetworkUtility.getConnectivityStatusString(context)
        Log.e("sandra", "onReceive: $status", )
        if(status == NetworkUtility.NETWORK_STATUS_NOT_CONNECTED){
            isOnline = false
            Log.e("sandra", "onReceive: not online", )
        }else{
            isOnline = true
            Log.e("sandra", "onReceive: online", )
        }
    }
}