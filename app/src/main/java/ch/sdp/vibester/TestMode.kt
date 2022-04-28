package ch.sdp.vibester


/**
 * This class represent a state manager that can be used in unit test
 * It may be used to disable certain features for unit test
 */
class TestMode private constructor(){
    companion object{
        private var isTest: Boolean = false

        /**
         * Setter for IsTest()
         */
        fun setTest(){
            isTest = true
        }

        /**
         * Is Test enabled
         */
        fun isTest(): Boolean {
            return isTest
        }
    }
}