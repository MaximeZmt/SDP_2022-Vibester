package ch.sdp.vibester.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PersistanceSetter private constructor(){
    companion object{
        private var alreadyExecuted : Boolean = false

        fun setPersistance(){
            if (!alreadyExecuted){
                Firebase.database.setPersistenceEnabled(true)
                alreadyExecuted = true
            }
        }

        fun getPersistance(): Boolean{
            return alreadyExecuted
        }
    }

}