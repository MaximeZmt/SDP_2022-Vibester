package ch.sdp.vibester

public class BuzzerScoreUpdater(scores: Array<Int>, ids: Array<Int>) {

    private lateinit var buzzerToScoreMap: LinkedHashMap<Int, Int>

    init {
        initMap(scores, ids)
    }

    private fun initMap(scores: Array<Int>, ids: Array<Int>) {
        var theMap = LinkedHashMap<Int, Int>()
        if (scores.size!=ids.size) {
            throw IllegalArgumentException()
        }
        var i = 0
        while (i < scores.size) {
            theMap.put(ids[i], scores[i])
        }
        buzzerToScoreMap = theMap
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
            throw IllegalArgumentException()
        }
        val updatedScore = buzzerToScoreMap.getOrDefault(id, 0) + 1 // should never get to default,
        buzzerToScoreMap.put(id, updatedScore)
    }
}