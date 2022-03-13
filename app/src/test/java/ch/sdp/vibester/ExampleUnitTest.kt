package ch.sdp.vibester

import ch.sdp.vibester.auth.FireBaseAuthenticator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun badRequest() {
//        val mockAuth = FireBaseAuthenticator()
//        val returnText = mockAuth.googleActivityResult(-1, -1, null)
//        assertEquals(returnText, "Authentication error")
//    }
}