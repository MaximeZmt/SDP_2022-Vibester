package ch.sdp.vibester

import kotlin.math.min

public class BuzzerScoreUpdater(ids: Array<Int>, scores: Array<Int>) {

    private var buzzerToScoreMap: LinkedHashMap<Int, Int>

    init {
        buzzerToScoreMap = initMap(ids, scores)
    }

    private fun initMap(ids: Array<Int>, scores: Array<Int>): LinkedHashMap<Int, Int> {
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
     * checks first if the given id
     */
    fun updateScoresArray(id: Int) {
        if (!buzzerToScoreMap.keys.contains(id)) {
            return
        }
        val updatedScore = buzzerToScoreMap.getOrDefault(id, 0) + 1 // should never get to default,
        buzzerToScoreMap.put(id, updatedScore)
    }
}