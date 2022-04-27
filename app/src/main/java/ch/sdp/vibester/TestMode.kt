package ch.sdp.vibester

class TestMode private constructor(){
    companion object{
        private var isTest: Boolean = false

        fun setTest(){
            isTest = true
        }

        fun isTest(): Boolean {
            return isTest
        }


    }
}