package ch.sdp.vibester.auth

import androidx.test.core.app.ApplicationProvider
import ch.sdp.vibester.TestMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FireBaseAuthenticatorTest {

    @Test
    fun checkTestModeGetMail(){
        TestMode.setTest()
        assertThrows(NullPointerException::class.java, {
            FireBaseAuthenticator.getCurrentUserMail()
        })
    }

    @Test
    fun checkTestModeGetUID(){
        TestMode.setTest()
        assertThrows(NullPointerException::class.java, {
            FireBaseAuthenticator.getCurrentUID()
        })
    }

    @Test
    fun checkGoogleFakeRequestCode(){
        assertEquals("Authentication error", FireBaseAuthenticator.googleActivityResult(0, 0, null, ApplicationProvider.getApplicationContext()))
    }

    @Test
    fun checkGoogleFakeGoodCode(){
        assertEquals("Authentication error", FireBaseAuthenticator.googleActivityResult(1000, 0, null, ApplicationProvider.getApplicationContext()))
    }
}