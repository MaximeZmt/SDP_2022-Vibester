package ch.sdp.vibester.util

class Util {
    companion object {
        fun createNewId(): String {
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val newId = (1..10)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");

            return newId
        }
    }

}