package ch.sdp.vibester.api

import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient

class LastfmHelper {
    companion object{
        private var pageLimitQueryTwo = 200;

        fun getBothtSongList(method: String, tag:String = ""): List<String> {
            val firstQuery = SongList(LastfmApi.querySongList(OkHttpClient(),
                LastfmUri(method = method, tag = tag)).get())
            val firstSongList = firstQuery.getSongList()
            val pagesQueryOne = firstQuery.getTotalPages().toInt()

            if(pagesQueryOne < pageLimitQueryTwo){
                pageLimitQueryTwo = pagesQueryOne
            }

            val pageQueryTwo =(2..pageLimitQueryTwo).random()
            val secondQuery = SongList(LastfmApi.querySongList(OkHttpClient(),
                LastfmUri(method = method, tag = tag, page = pageQueryTwo.toString())).get())
            val secondSongList = secondQuery.getSongList()

            val elementsOne = 2
            val elementsTwo = 8
            val randomElementsOne = firstSongList.asSequence().shuffled().take(elementsOne).toList()
            var randomElementsTwo = secondSongList.asSequence().shuffled().take(elementsTwo).toList()
            val finalList = merge(randomElementsOne, randomElementsTwo)
            return finalList
        }

        fun <T> merge(first: List<T>, second: List<T>): List<T> {
            return first + second
        }
    }
}