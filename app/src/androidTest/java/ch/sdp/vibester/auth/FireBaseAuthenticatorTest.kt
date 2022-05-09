package ch.sdp.vibester.auth

import ch.sdp.vibester.TestMode
import org.junit.Assert.assertEquals
import org.junit.Test

class FireBaseAuthenticatorTest {

    @Test
    fun checkTestModeGetMail(){
        TestMode.setTest()
        assertEquals("", FireBaseAuthenticator().getCurrUserMail())
    }

    @Test
    fun checkTestModeGetUID(){
        TestMode.setTest()
        assertEquals("", FireBaseAuthenticator().getCurrUID())
    }
}