package ch.sdp.vibester.api

import android.content.Context
import android.net.ConnectivityManager

class InternetState private constructor(){
    companion object{
        private var forceOffline: Boolean = false
        private var hasAlreadyAccessedInternetOnce: Boolean = false

        /**
         * Get Internet Connection Status
         * @return return true if connected; false otherwise
         */
        fun getInternetStatus(ctx: Context): Boolean {
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            var isNetwork: Boolean = false

            if(networkInfo != null){
                isNetwork = networkInfo.isConnected
            }
            return isNetwork && !forceOffline
        }

        /**
         * Force offline answer from getInternetStatus
         */
        fun forceOffline() {
           forceOffline = true
        }

        /**
         * Has the app already accessed internet since it has started
         * @return return true if connected; false otherwise
         */
        fun hasAccessedInternetOnce(ctx: Context): Boolean {
            if (!hasAlreadyAccessedInternetOnce && getInternetStatus(ctx)) {
                hasAlreadyAccessedInternetOnce = true
            }
            return hasAlreadyAccessedInternetOnce
        }

    }
}