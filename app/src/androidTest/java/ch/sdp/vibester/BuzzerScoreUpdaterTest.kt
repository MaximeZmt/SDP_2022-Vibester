package ch.sdp.vibester

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

public class BuzzerScoreUpdaterTest {

    @Test
    fun constructorTestWithSameSizedArrays() {
        val idArray = arrayOf(0, 1, 2, 3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val expectedMap = LinkedHashMap<Int, Int>()
        expectedMap.put(0, 0)
        expectedMap.put(1, 0)
        expectedMap.put(2, 0)
        expectedMap.put(3, 0)
        val testUpdater = BuzzerScoreUpdater(scoreArray, idArray)
        assert(testUpdater.getMap()==(expectedMap)) // value or reference?
    }

    @Test
    fun arrayUpdateTest() {

    }
}
