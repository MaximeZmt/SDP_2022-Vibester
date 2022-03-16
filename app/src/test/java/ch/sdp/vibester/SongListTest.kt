package ch.sdp.vibester

import ch.sdp.vibester.model.SongList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SongListTest {

    @Test
    fun jsonPreviewParseCorrectText(){
            val inputTxt = """
            {"tracks":
            {"track":[{"name":"Wonderwall","duration":"259","mbid":"31623cce-9717-4513-9d83-1b5d04e44f9b",
            "url":"https://www.last.fm/music/Oasis/_/Wonderwall",
            "streamable":{"#text":"0","fulltrack":"0"},
            "artist":{"name":"Oasis","mbid":"ecf9f3a3-35e9-4c58-acaa-e707fba45060","url":"https://www.last.fm/music/Oasis"},
            "image":[{"#text":"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"small"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"medium"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png","size":"large"},
            {"#text":"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png","size":"extralarge"}],
            "@attr":{"rank":"1"}}],"@attr":{"tag":"british","page":"1","perPage":"1","totalPages":"66649","total":"66649"}}}
            """

            val mySongsList = SongList(inputTxt)

            val songName = "Wonderwall"
            val artistName = "Oasis"
            val inputSongsList = mutableListOf<String>("$songName $artistName")
            val page = "1"
            val songsPerPage = "1"
            val totalPages = "66649"
            val totalSongs = "66649"


            assertEquals(inputSongsList, mySongsList.getSongList())
            assertEquals(page, mySongsList.getPage())
            assertEquals(songsPerPage, mySongsList.getSongsPerPage())
            assertEquals(totalPages, mySongsList.getTotalPages())
            assertEquals(totalSongs, mySongsList.getTotalSongs())
    }

    @get:Rule
    var exception = ExpectedException.none()

    @Test
    fun jsonPreviewParseErrorText() {
        exception.expect(IllegalArgumentException::class.java)
        exception.expectMessage("SongsList constructor, bad argument")
        SongList("")
    }

}