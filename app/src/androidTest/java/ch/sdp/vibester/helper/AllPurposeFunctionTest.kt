package ch.sdp.vibester.helper

import org.junit.Assert.assertEquals
import org.junit.Test

class AllPurposeFunctionTest {

    @Test
    fun testBiggerInt(){
        val i1 = 1
        val i2 = 2
        assertEquals(i2, AllPurposeFunction.biggerInt(i1, i2))
        assertEquals(i2, AllPurposeFunction.biggerInt(i2, i1))
    }
}