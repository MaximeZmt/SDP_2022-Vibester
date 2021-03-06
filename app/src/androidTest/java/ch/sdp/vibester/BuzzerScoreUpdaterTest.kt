package ch.sdp.vibester

import junit.framework.Assert.assertEquals
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
            assertTrue(testUpdater.getMap()[id] == 1)
        }
    }

    @Test
    fun arrayUpdateWithWrongIdReturns() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        testUpdater.updateScoresArray(-1)
        for (id in idArray) {
            assertEquals(0, testUpdater.getMap()[id])
        }
    }

    @Test
    fun noWinnerTest() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(0, 0, 0, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        val testWinner = testUpdater.computeWinner()
        assertEquals(0, testWinner.size)
    }

    @Test
    fun oneWinnerTest() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(1, 0, 2, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        val testWinner = testUpdater.computeWinner()
        assertEquals(1, testWinner.size)
        assertEquals(R.id.buzzer_2, testWinner.get(0))
    }

    @Test
    fun moreThanOneWinnerTest() {
        val idArray = arrayListOf(R.id.buzzer_0, R.id.buzzer_1, R.id.buzzer_2, R.id.buzzer_3)
        val scoreArray = arrayOf(2, 1, 2, 0)
        val testUpdater = BuzzerScoreUpdater(idArray, scoreArray)
        val testWinner = testUpdater.computeWinner()
        assertEquals(2, testWinner.size)
        assertTrue(testWinner.contains(R.id.buzzer_0) && testWinner.contains(R.id.buzzer_2))
    }
}
