package ch.sdp.vibester.api

import android.content.Context

interface MusicApi {
    /**
     * Given a String query it will provide the music url preview
     */
    fun findUrlPreview(query: String, ctx: Context)



}