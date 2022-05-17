package ch.sdp.vibester.database

import junit.framework.Assert.assertEquals
import org.junit.Test

class PersistanceSetterTest {
    @Test
    fun testPersistance() {
        PersistenceSetter.setPersistence()
        assertEquals(true, PersistenceSetter.getPersistence())
    }
}