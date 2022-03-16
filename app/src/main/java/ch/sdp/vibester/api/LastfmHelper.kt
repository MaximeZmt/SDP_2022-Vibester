package ch.sdp.vibester.api

import ch.sdp.vibester.model.SongList
import okhttp3.OkHttpClient

class LastfmHelper {
    companion object{
        private var pageLimitQueryTwo = 200;

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

            val mergedLists = merge(firstSongList, secondSongList)
            var finalList = mergedLists.asSequence().shuffled().take(GAME_SIZE).toList()
            return finalList
        }

        fun <T> merge(first: List<T>, second: List<T>): List<T> {
            return first + second
        }
    }
}