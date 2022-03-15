package ch.sdp.vibester.model

import com.google.gson.annotations.SerializedName


class Lyric {
    @SerializedName("lyrics")
    var lyrics: String? = null
    //not used
    //@SerializedName("error")
    //var error: String? = null
}