package ch.sdp.vibester.api

import android.content.Context
import android.net.ConnectivityManager

class InternetState private constructor(){
    companion object{
        /**
         * Get Internet Connection Status
         * @return return true if connected; false otherwise
         */
        fun getInternetStatus(ctx: Context): Boolean{
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            var isNetwork: Boolean = false

            if(networkInfo != null){
                isNetwork = networkInfo.isConnected
            }
            return isNetwork
        }

        private var hasAlreadyAccessedInternetOnce: Boolean = false
        fun hasAccessedInternetOnce(ctx: Context):Boolean{
            if (!hasAlreadyAccessedInternetOnce && getInternetStatus(ctx)) {
                hasAlreadyAccessedInternetOnce = true
            }
            return hasAlreadyAccessedInternetOnce
        }

    }
}