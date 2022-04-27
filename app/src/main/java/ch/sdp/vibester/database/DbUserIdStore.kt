package ch.sdp.vibester.database

class DbUserIdStore private constructor(){
    companion object{
        private var uid: String = ""
        fun storeUID(uid: String){
            this.uid = uid
        }

        fun getUID(): String{
            return uid
        }

    }
}