package iti.mad42.weathery.model.pojo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkUtility {
    companion object{
        val TYPE_WIFI = 1
        val TYPE_MOBILE = 2
        val TYPE_NOT_CONNECTED = 0
        val NETWORK_STATUS_WIFI = 1
        val NETWORK_STATUS_MOBILE = 2
        val NETWORK_STATUS_NOT_CONNECTED = 0

        @RequiresApi(Build.VERSION_CODES.M)
        fun getConnectivityStatus(context: Context) : Int{
            var connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            var network  = connectivityManager.activeNetwork?: return TYPE_NOT_CONNECTED
            val activeNetwork = connectivityManager.getNetworkCapabilities(network)?: return TYPE_NOT_CONNECTED
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> TYPE_WIFI
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> TYPE_MOBILE
                else -> TYPE_NOT_CONNECTED
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun getConnectivityStatusString(context: Context) : Int{
            var conn = NetworkUtility.getConnectivityStatus(context)
            var status = 0
            if(conn == NetworkUtility.TYPE_WIFI){
                status = NETWORK_STATUS_WIFI
            }else if(conn == NetworkUtility.TYPE_MOBILE){
                status = NETWORK_STATUS_MOBILE
            }else if(conn == NetworkUtility.TYPE_NOT_CONNECTED){
                status = NETWORK_STATUS_NOT_CONNECTED
            }
            return status
        }

    }
}