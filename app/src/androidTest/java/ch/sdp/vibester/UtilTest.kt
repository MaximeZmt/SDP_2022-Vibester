package ch.sdp.vibester

import ch.sdp.vibester.util.Util
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilTest {
    @Test
    fun newIdIsCorrectLength() {
        assertEquals(Util.createNewId().length, 10)
    }
}