package ch.sdp.vibester.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Enable Persistance in Firebase database.
 * If internet connection is lost while app is on, it continues to work
 */
class PersistanceSetter private constructor(){
    companion object{
        private var alreadyExecuted : Boolean = false

        /**
         * Turn on the persistance
         */
        fun setPersistance(){
            if (!alreadyExecuted) {
                Firebase.database.setPersistenceEnabled(true)
                alreadyExecuted = true
            }
        }

        /**
         * Getter to check whether it has been turned on
         * @return Boolean
         */
        fun getPersistance(): Boolean{
            return alreadyExecuted
        }
    }

}