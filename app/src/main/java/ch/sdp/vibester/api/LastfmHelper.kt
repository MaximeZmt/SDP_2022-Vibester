//package ch.sdp.vibester.api
//
//import ch.sdp.vibester.model.SongList
//import okhttp3.OkHttpClient
//
///**
// * Object to combine the song lists from two Lastfm queries.
// * Two queries helps to achieve better randomization of the songs.
// * Final song list for a game is created from the songs fetched from Page 1 and
// * Page Random.
// *
// * The limit to choose page for the second query is 200 due to Lastfm bug.
// * (songs do not appear on page 201 and higher)
// */
//class LastfmHelper {
//
//    companion object{
//        private var pageLimitQueryTwo = 200;
//        private val GAME_SIZE = 30;
//
//        /**
//         * Function that performs queries to two pages within given tag
//         * @param method: method to retrieve songs BY_TAG or BY_CHART
//         * @param tag: tag to retrieve songs. If method BY_CHART is used, tag is empty.
//         * @return list of songs in a format ("$songName $artistName")
//         */
//        fun getRandomSongList(jsonMeta: String): List<String> {
////            uri.limit = "100"
////            val res = LastfmApi.querySongList(OkHttpClient(),uri).get();
//            val firstQuery = SongList(jsonMeta)
//            val firstSongList = firstQuery.getSongList()
//            val pagesQueryOne = firstQuery.getTotalPages().toInt()
//
////            if(pagesQueryOne < pageLimitQueryTwo){
////                pageLimitQueryTwo = pagesQueryOne
////            }
//
////            val pageQueryTwo =(2..pageLimitQueryTwo).random()
////            val secondQuery = SongList(LastfmApi.querySongList(OkHttpClient(),
////                LastfmUri(method = method, tag = tag, page = pageQueryTwo.toString())).get())
////            val secondSongList = secondQuery.getSongList()
////
////            val mergedLists = merge(firstSongList, secondSongList)
//            var finalList = firstSongList.asSequence().shuffled().take(GAME_SIZE).toList()
//            return finalList
//        }
//
//        fun <T> merge(first: List<T>, second: List<T>): List<T> {
//            return first + second
//        }
//    }
//}