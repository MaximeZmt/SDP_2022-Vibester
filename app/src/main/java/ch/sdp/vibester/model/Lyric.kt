package ch.sdp.vibester.model

import com.google.gson.annotations.SerializedName


class Lyric {
    @SerializedName("lyrics")
    var lyrics: String? = null
    @SerializedName("error")
    var error: String? = null
}