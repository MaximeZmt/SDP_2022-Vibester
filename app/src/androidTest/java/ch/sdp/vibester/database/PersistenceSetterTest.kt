package ch.sdp.vibester.database

import junit.framework.Assert.assertEquals
import org.junit.Test

class PersistenceSetterTest {
    @Test
    fun testPersistence() {
        PersistenceSetter.setPersistence()
        assertEquals(true, PersistenceSetter.getPersistence())
    }
}