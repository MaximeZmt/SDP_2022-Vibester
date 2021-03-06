package ch.sdp.vibester.model


import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class SongTest {

    @Test
    fun jsonPreviewParseBasic() {
        val inputTxt = """
            {
                "resultCount":1,
                "results": [
                {"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4",
                    "previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
            }
            """

        val mySong = Song.singleSong(inputTxt)

        val previewUrl =
            "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a"
        val artworkUrl =
            "https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg"
        val artistName = "Imagine Dragons"
        val trackName = "Monday"


        assertEquals(previewUrl, mySong.getPreviewUrl())
        assertEquals(artworkUrl, mySong.getArtworkUrl())
        assertEquals(artistName, mySong.getArtistName())
        assertEquals(trackName, mySong.getTrackName())
    }


    @get:Rule
    var exception: ExpectedException = ExpectedException.none()

    @Test
    fun jsonPreviewParseErrorText() {
        exception.expect(IllegalArgumentException::class.java)
        exception.expectMessage("Song constructor, bad argument")
        Song.singleSong("")
    }

    @Test
    fun test() {
        val test = Song.listSong(
            """
    {
 "resultCount":13,
 "results": [
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1574210519, "trackId":1574210894, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1574210519?i=1574210894&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/bc/71/fc/bc71fca4-e0bb-609b-5b6e-92296df7b4b6/mzaf_8907306752631175088.plus.aac.p.m4a", "artworkUrl30":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/30x30bb.jpg", "artworkUrl60":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/60x60bb.jpg", "artworkUrl100":"https://is3-ssl.mzstatic.com/image/thumb/Music115/v4/3e/04/c4/3e04c4e7-1863-34cb-e8f3-f168ae5b213e/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":13, "trackNumber":4, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1607399614, "trackId":1607400053, "artistName":"Imagine Dragons", "collectionName":"Mercury - Act 1 (Additional Track Version)", "trackName":"Monday", "collectionCensoredName":"Mercury - Act 1 (Additional Track Version)", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1607399614?i=1607400053&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1607399614?i=1607400053&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview126/v4/7d/2c/53/7d2c53b7-b30d-5929-187b-b4de4bf2b632/mzaf_5122873789896036251.plus.aac.p.m4a", "artworkUrl30":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/7c/89/5f/7c895f88-cf16-24d2-c7a9-b7d532f34d2e/source/30x30bb.jpg", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/7c/89/5f/7c895f88-cf16-24d2-c7a9-b7d532f34d2e/source/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/7c/89/5f/7c895f88-cf16-24d2-c7a9-b7d532f34d2e/source/100x100bb.jpg", "collectionPrice":9.99, "trackPrice":1.29, "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":14, "trackNumber":5, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1592583491, "trackId":1592584060, "artistName":"Imagine Dragons", "collectionName":"Happier Than Ever ???", "trackName":"Monday", "collectionCensoredName":"Happier Than Ever ???", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1592583491?i=1592584060&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1592583491?i=1592584060&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/ce/a8/43/cea84379-6a85-cc33-da03-ac667519e842/mzaf_11236287847568272512.plus.aac.p.m4a", "artworkUrl30":"https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/81/ab/76/81ab7633-1f15-5e2f-9f08-8ff04d88a470/source/30x30bb.jpg", "artworkUrl60":"https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/81/ab/76/81ab7633-1f15-5e2f-9f08-8ff04d88a470/source/60x60bb.jpg", "artworkUrl100":"https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/81/ab/76/81ab7633-1f15-5e2f-9f08-8ff04d88a470/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":30, "trackNumber":22, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1598234911, "trackId":1598235539, "artistName":"Imagine Dragons", "collectionName":"New Years Eve Dinner", "trackName":"Monday", "collectionCensoredName":"New Years Eve Dinner", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1598234911?i=1598235539&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1598234911?i=1598235539&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/90/f0/83/90f08388-c895-4a4a-649b-8674ee50c96b/mzaf_3834484160786471912.plus.aac.p.m4a", "artworkUrl30":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/28/66/db/2866dbac-8a1c-6aa6-547a-62537853c566/source/30x30bb.jpg", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/28/66/db/2866dbac-8a1c-6aa6-547a-62537853c566/source/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/28/66/db/2866dbac-8a1c-6aa6-547a-62537853c566/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":49, "trackNumber":10, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1590204361, "trackId":1590205202, "artistName":"Imagine Dragons", "collectionName":"Autumn Vibes 2021", "trackName":"Monday", "collectionCensoredName":"Autumn Vibes 2021", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1590204361?i=1590205202&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1590204361?i=1590205202&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/c8/2a/22/c82a220a-1ac0-a04b-5b08-237c8b144cd8/mzaf_13661380597121457102.plus.aac.p.m4a", "artworkUrl30":"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/d8/52/35/d8523521-d248-ea79-9053-d2367768da8d/source/30x30bb.jpg", "artworkUrl60":"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/d8/52/35/d8523521-d248-ea79-9053-d2367768da8d/source/60x60bb.jpg", "artworkUrl100":"https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/d8/52/35/d8523521-d248-ea79-9053-d2367768da8d/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":51, "trackNumber":18, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1590899423, "trackId":1590900560, "artistName":"Imagine Dragons", "collectionName":"Herbst Chill Out", "trackName":"Monday", "collectionCensoredName":"Herbst Chill Out", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1590899423?i=1590900560&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1590899423?i=1590900560&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/89/81/4a/89814aec-03c9-1c12-2980-0af710bb1a7a/mzaf_3448969610634166831.plus.aac.p.m4a", "artworkUrl30":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/46/93/c6/4693c6e6-15d6-fa21-a4cf-3330c2f323c4/source/30x30bb.jpg", "artworkUrl60":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/46/93/c6/4693c6e6-15d6-fa21-a4cf-3330c2f323c4/source/60x60bb.jpg", "artworkUrl100":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/46/93/c6/4693c6e6-15d6-fa21-a4cf-3330c2f323c4/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":31, "trackNumber":24, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1595654464, "trackId":1595655177, "artistName":"Imagine Dragons", "collectionName":"Let's Go To The Sea", "trackName":"Monday", "collectionCensoredName":"Let's Go To The Sea", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1595654464?i=1595655177&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1595654464?i=1595655177&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/72/38/e3/7238e330-b071-800a-5eff-f2c278d1ded1/mzaf_13237911137918017214.plus.aac.p.m4a", "artworkUrl30":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/ae/ad/9a/aead9aae-112c-54b8-4f82-3158f2d878f0/source/30x30bb.jpg", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/ae/ad/9a/aead9aae-112c-54b8-4f82-3158f2d878f0/source/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/ae/ad/9a/aead9aae-112c-54b8-4f82-3158f2d878f0/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":30, "trackNumber":12, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1590052407, "trackId":1590052409, "artistName":"Imagine Dragons", "collectionName":"Autumn 2021", "trackName":"Monday", "collectionCensoredName":"Autumn 2021", "trackCensoredName":"Monday", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/monday/1590052407?i=1590052409&uo=4", "trackViewUrl":"https://music.apple.com/us/album/monday/1590052407?i=1590052409&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview116/v4/10/a3/6e/10a36ef9-945d-e677-1971-81d32333ae08/mzaf_10441392226409654424.plus.aac.p.m4a", "artworkUrl30":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/ba/6a/e4/ba6ae459-e963-a006-9f43-a6a55a2d98c4/source/30x30bb.jpg", "artworkUrl60":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/ba/6a/e4/ba6ae459-e963-a006-9f43-a6a55a2d98c4/source/60x60bb.jpg", "artworkUrl100":"https://is4-ssl.mzstatic.com/image/thumb/Music125/v4/ba/6a/e4/ba6ae459-e963-a006-9f43-a6a55a2d98c4/source/100x100bb.jpg", "releaseDate":"2021-09-03T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":26, "trackNumber":1, "trackTimeMillis":187896, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"music-video", "artistId":358714030, "trackId":1587060436, "artistName":"Imagine Dragons", "trackName":"Monday", "trackCensoredName":"Monday", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "trackViewUrl":"https://music.apple.com/us/music-video/monday/1587060436?uo=4", 
"previewUrl":"https://video-ssl.itunes.apple.com/itunes-assets/Video115/v4/c7/22/cf/c722cfdd-2bb3-2edc-7ad5-20ec02e66b02/mzvf_6062793501036988347.640x480.h264lc.U.p.m4v", "artworkUrl30":"https://is1-ssl.mzstatic.com/image/thumb/Video125/v4/c9/8e/03/c98e0316-88ad-782c-7a45-ce5b0884afe9/source/30x30bb.jpg", "artworkUrl60":"https://is1-ssl.mzstatic.com/image/thumb/Video125/v4/c9/8e/03/c98e0316-88ad-782c-7a45-ce5b0884afe9/source/60x60bb.jpg", "artworkUrl100":"https://is1-ssl.mzstatic.com/image/thumb/Video125/v4/c9/8e/03/c98e0316-88ad-782c-7a45-ce5b0884afe9/source/100x100bb.jpg", "collectionPrice":1.99, "trackPrice":1.99, "releaseDate":"2021-09-17T07:00:00Z", "collectionExplicitness":"notExplicit", "trackExplicitness":"notExplicit", "trackTimeMillis":227817, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative"}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1579947713, "trackId":1579948392, "artistName":"Imagine Dragons", "collectionName":"Monday Motivation", "trackName":"Believer", "collectionCensoredName":"Monday Motivation", "trackCensoredName":"Believer", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/believer/1579947713?i=1579948392&uo=4", "trackViewUrl":"https://music.apple.com/us/album/believer/1579947713?i=1579948392&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/72/b8/b4/72b8b40b-6b9c-38c8-2b3c-52b61b35e1f7/mzaf_9678877204506190269.plus.aac.p.m4a", "artworkUrl30":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/30x30bb.jpg", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/100x100bb.jpg", "releaseDate":"2017-02-01T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":26, "trackNumber":15, "trackTimeMillis":204347, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1579947713, "trackId":1579947725, "artistName":"Imagine Dragons", "collectionName":"Monday Motivation", "trackName":"Radioactive", "collectionCensoredName":"Monday Motivation", "trackCensoredName":"Radioactive", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/radioactive/1579947713?i=1579947725&uo=4", "trackViewUrl":"https://music.apple.com/us/album/radioactive/1579947713?i=1579947725&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/fc/41/bf/fc41bffa-1ec1-507b-6f1b-f9cc392d4b01/mzaf_1144355550048099037.plus.aac.p.m4a", "artworkUrl30":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/30x30bb.jpg", "artworkUrl60":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/60x60bb.jpg", "artworkUrl100":"https://is2-ssl.mzstatic.com/image/thumb/Music125/v4/f0/58/d0/f058d0bf-a341-5ed1-63fe-46062ed54b7d/source/100x100bb.jpg", "releaseDate":"2012-01-01T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":26, "trackNumber":3, "trackTimeMillis":188000, "country":"USA", "currency":"USD", "primaryGenreName":"Pop", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1577842198, "trackId":1577842203, "artistName":"Imagine Dragons", "collectionName":"Monday Booster", "trackName":"Believer", "collectionCensoredName":"Monday Booster", "trackCensoredName":"Believer", "collectionArtistId":36270, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/believer/1577842198?i=1577842203&uo=4", "trackViewUrl":"https://music.apple.com/us/album/believer/1577842198?i=1577842203&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/4d/d8/3d/4dd83d3c-67fe-4674-c684-054662e95fa7/mzaf_12428085415395638778.plus.aac.p.m4a", "artworkUrl30":"https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/7c/73/15/7c7315d8-8717-7552-5588-c7966ec1ae69/source/30x30bb.jpg", "artworkUrl60":"https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/7c/73/15/7c7315d8-8717-7552-5588-c7966ec1ae69/source/60x60bb.jpg", "artworkUrl100":"https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/7c/73/15/7c7315d8-8717-7552-5588-c7966ec1ae69/source/100x100bb.jpg", "releaseDate":"2017-02-01T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":29, "trackNumber":1, "trackTimeMillis":204347, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}, 
{"wrapperType":"track", "kind":"song", "artistId":358714030, "collectionId":1442220504, "trackId":1442220631, "artistName":"Imagine Dragons", "collectionName":"Man Crush Monday", "trackName":"Thunder", "collectionCensoredName":"Man Crush Monday", "trackCensoredName":"Thunder", "collectionArtistId":4035426, "collectionArtistName":"Various Artists", "artistViewUrl":"https://music.apple.com/us/artist/imagine-dragons/358714030?uo=4", "collectionViewUrl":"https://music.apple.com/us/album/thunder/1442220504?i=1442220631&uo=4", "trackViewUrl":"https://music.apple.com/us/album/thunder/1442220504?i=1442220631&uo=4", 
"previewUrl":"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview126/v4/d8/77/70/d8777065-cffe-9e87-5060-9e9f99cba15b/mzaf_16624547533184608282.plus.aac.p.m4a", "artworkUrl30":"https://is4-ssl.mzstatic.com/image/thumb/Music114/v4/56/d3/71/56d37114-9b1d-3ddd-af77-7dae4389d2d2/source/30x30bb.jpg", "artworkUrl60":"https://is4-ssl.mzstatic.com/image/thumb/Music114/v4/56/d3/71/56d37114-9b1d-3ddd-af77-7dae4389d2d2/source/60x60bb.jpg", "artworkUrl100":"https://is4-ssl.mzstatic.com/image/thumb/Music114/v4/56/d3/71/56d37114-9b1d-3ddd-af77-7dae4389d2d2/source/100x100bb.jpg", "releaseDate":"2017-04-27T12:00:00Z", "collectionExplicitness":"explicit", "trackExplicitness":"notExplicit", "discCount":1, "discNumber":1, "trackCount":23, "trackNumber":2, "trackTimeMillis":188198, "country":"USA", "currency":"USD", "primaryGenreName":"Alternative", "isStreamable":true}]
}
    """
        )
        println(test[0].getTrackName())
    }

    @Test
    fun songBuilderTest() {
        val songName = "test"
        val songArtist = "Singer"
        val artworkUrl = "https://none.com"
        val previewUrl = "https://none.com"

        val mySong = Song.songBuilder(previewUrl, artworkUrl, songName, songArtist)
        val mySong2 = Song.songBuilder()

        assertEquals(songName, mySong.getTrackName())
        assertEquals(songArtist, mySong.getArtistName())
        assertEquals(artworkUrl, mySong.getArtworkUrl())
        assertEquals(previewUrl, mySong.getPreviewUrl())

        assertEquals("", mySong2.getTrackName())
        assertEquals("", mySong2.getArtistName())
        assertEquals("", mySong2.getArtworkUrl())
        assertEquals("", mySong2.getPreviewUrl())
    }

    @Test
    fun testBadArgument() {
        assertThrows(IllegalArgumentException::class.java) { Song(JSONObject("{}")) }
        assertThrows(IllegalArgumentException::class.java) { Song.listSong("{}") }
    }

}