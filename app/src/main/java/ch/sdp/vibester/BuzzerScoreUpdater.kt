package ch.sdp.vibester

import kotlin.math.max
import kotlin.math.min

class BuzzerScoreUpdater(ids: ArrayList<Int>, scores: Array<Int>) {

    private var buzzerToScoreMap: LinkedHashMap<Int, Int>

    init {
        buzzerToScoreMap = initMap(ids, scores)
    }

    private fun initMap(ids: ArrayList<Int>, scores: Array<Int>): LinkedHashMap<Int, Int> {
        var theMap = LinkedHashMap<Int, Int>()
        var i = 0
        while (i < min(ids.size, scores.size)) {
            theMap[ids[i]] = scores[i]
            i = i + 1
        }
        return theMap
    }

    fun getMap(): LinkedHashMap<Int, Int> {
        return buzzerToScoreMap
    }

    /**
     * updates the score corresponding to the pressed buzzer
     * checks first if the given id is a buzzer id (and not NO_BUZZER_PRESSED)
     */
    fun updateScoresArray(id: Int, answerCorrect: Boolean) {
        if (!buzzerToScoreMap.keys.contains(id)) {
            return
        }
        val point = if (answerCorrect) {1} else {0}
        val updatedScore = max(buzzerToScoreMap.getOrDefault(id, 0) + point, 0) // should never get to default
        buzzerToScoreMap.put(id, updatedScore)
    }

    /**
     * Computes which player wins the game according to the scores in the array
     * @return an arrayList with:
     * - no elements if everyone's score is zero
     * - one element if there is one winner
     * - more than one element if two or more players are tied at a non-zero score
     */
    fun computeWinner(): ArrayList<Int> {
        var winner = arrayListOf<Int>()
        var highScore: Int = 0
        for (player in buzzerToScoreMap.keys) {
            val testedScore = buzzerToScoreMap[player]
            if (testedScore != null) {
                if (testedScore > max(0, highScore)) {
                    winner.clear()
                    winner.add(player)
                    highScore = testedScore
                } else if (testedScore > 0 && testedScore == highScore) {
                    winner.add(player)
                }
            }
        }
        return winner
    }
}