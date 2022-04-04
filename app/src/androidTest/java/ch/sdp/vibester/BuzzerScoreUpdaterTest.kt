package ch.sdp.vibester

import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

public class BuzzerScoreUpdaterTest {

    @Test
    fun constructorTestWithSameSizedArrays() {
        val idArray = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        for (key in idArray) {
            assertTrue(testUpdater.getMap().keys.contains(key))
        }
        for (value in scoreArray) {
            assertTrue(testUpdater.getMap().values.contains(value))
        }
    }

    @Test
    fun arrayUpdateTest() {
        val idArray = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        for (id in idArray) {
            testUpdater.updateScoresArray(id, 1)
            assertTrue(testUpdater.getMap()[id]==1)
        }
    }

    @Test
    fun arrayUpdateWithWrongIdReturns() {
        val idArray = arrayOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        testUpdater.updateScoresArray(-1, 1)
        for (id in idArray) {
            assertTrue(testUpdater.getMap()[id]==0)
        }
    }
}
