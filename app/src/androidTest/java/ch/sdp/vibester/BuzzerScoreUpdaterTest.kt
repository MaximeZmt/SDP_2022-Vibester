package ch.sdp.vibester

import junit.framework.Assert.assertTrue
import org.junit.Test

public class BuzzerScoreUpdaterTest {

    @Test
    fun constructorTestWithSameSizedArrays() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
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
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        for (id in idArray) {
            testUpdater.updateScoresArray(id)
            assertTrue(testUpdater.getMap()[id]==1)
        }
    }

    @Test
    fun arrayUpdateWithWrongIdReturns() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        testUpdater.updateScoresArray(-1)
        for (id in idArray) {
            assertTrue(testUpdater.getMap()[id]==0)
        }
    }
}
