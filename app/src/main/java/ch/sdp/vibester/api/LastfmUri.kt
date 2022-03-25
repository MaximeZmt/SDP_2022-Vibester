package ch.sdp.vibester.api

data class LastfmUri (
    val method:String="",
    val format:String="json",
    val tag: String = "",
    val page: String = "1",
    var limit: String = "100",
    val artist: String = ""
)