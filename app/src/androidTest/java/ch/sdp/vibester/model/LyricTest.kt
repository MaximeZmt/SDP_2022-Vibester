package ch.sdp.vibester.model

import junit.framework.Assert.assertEquals
import org.junit.Test

class LyricTest {
    @Test
    fun testLyric() {
        val value = "Hello world"
        val lyric = Lyric()
        lyric.lyrics = value
        assertEquals(value, lyric.lyrics)
    }
}