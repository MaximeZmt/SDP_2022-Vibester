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

        private var isTest2: Boolean = false

        fun setTest2(){
            isTest2 = true
        }

        fun isTest2(): Boolean {
            return isTest2
        }
    }
}