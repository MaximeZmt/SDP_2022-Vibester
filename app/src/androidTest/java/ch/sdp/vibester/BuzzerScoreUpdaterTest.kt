package ch.sdp.vibester

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

public class BuzzerScoreUpdaterTest {

    @Test
    fun constructorTestWithSameSizedArrays() {
        val idArray = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val expectedMap = LinkedHashMap<Int, Int>()
        expectedMap.put(R.id.buzzer_0, 0)
        expectedMap.put(R.id.buzzer_1, 0)
        expectedMap.put(R.id.buzzer_2, 0)
        expectedMap.put(R.id.buzzer_3, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        for (key in idArray) {
            assert(testUpdater.getMap().keys.contains(key))
        }
        for (value in scoreArray) {
            assert(testUpdater.getMap().values.contains(value))
        }
    }

    @Test
    fun arrayUpdateTest() {
        val idArray = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        for (id in idArray) {
            testUpdater.updateScoresArray(id)
            assert(testUpdater.getMap()[id]==1)
        }
    }

}
