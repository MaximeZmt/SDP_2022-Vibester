package ch.sdp.vibester.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Enable Persistence in Firebase database.
 * If internet connection is lost while app is on, it continues to work
 */
class PersistenceSetter private constructor(){
    companion object{
        private var alreadyExecuted : Boolean = false

        /**
         * Turn on the persistence
         */
        fun setPersistence(){
            if (!alreadyExecuted) {
                Firebase.database.setPersistenceEnabled(true)
                alreadyExecuted = true
            }
        }

        /**
         * Getter to check whether it has been turned on
         * @return Boolean
         */
        fun getPersistence(): Boolean{
            return alreadyExecuted
        }
    }

}