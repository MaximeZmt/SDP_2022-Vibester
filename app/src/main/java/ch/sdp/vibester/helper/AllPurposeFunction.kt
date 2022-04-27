package ch.sdp.vibester.helper

class AllPurposeFunction private constructor(){
    companion object{

        /**
         * Functions that return the biggest value between the two given in argument
         * @param i1 first int value to be compared
         * @param i2 second int value to be compared
         * @return The biggest Int
         */
        fun biggerInt(i1: Int, i2: Int): Int{
            if (i1 > i2){
                return i1
            }else{
                return i2
            }
        }
    }
}