package ch.sdp.vibester.api

import junit.framework.Assert.assertTrue
import org.junit.Test

class LastfmHelperTest {
    private val BY_TAG = "tag.gettoptracks"
    private val BY_CHART = "chart.gettoptracks"

    @Test
    fun mergeWorks(){
        val listOne = listOf<String>("One","Two")
        val listTwo = listOf<String>("Three", "Four")
        val finalList = listOf<String>("One","Two","Three","Four")
        val test = LastfmHelper.merge(listOne, listTwo)
        assertTrue(test.size == finalList.size && test.containsAll(finalList) && finalList.containsAll(test));
    }

    @Test
    fun getBothtSongListWorks(){
        assertTrue(LastfmHelper.getBothtSongList(method = BY_TAG, tag = "kpop").size == 10)
    }
}