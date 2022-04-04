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
    fun updateScoresArray(id: Int, point: Int) {
        if (!buzzerToScoreMap.keys.contains(id)) {
            return
        }
        val updatedScore = max(buzzerToScoreMap.getOrDefault(id, 0) + point, 0) // should never get to default,
        buzzerToScoreMap.put(id, updatedScore)
    }
}