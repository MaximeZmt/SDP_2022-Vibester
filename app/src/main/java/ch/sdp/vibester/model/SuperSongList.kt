package ch.sdp.vibester.model

open class SuperSongList {
    var songList = mutableListOf<Pair<String, String>>()

    /**
     * Getter that return shuffled song list
     * @return MutableList<Pair<String,String>> of type Pair("$songName", "$artistName")
     */
    fun getShuffledSongList(): MutableList<Pair<String, String>> {
        return songList.asSequence().shuffled().toMutableList()
    }
}