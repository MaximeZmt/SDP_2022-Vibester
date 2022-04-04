package ch.sdp.vibester.api

data class LastfmUri(
    var method: String = "",
    val format: String = "json",
    var tag: String = "",
    val page: String = "1",
    var limit: String = "100",
    var artist: String = "",
    val api_key: String = "52bfdc690dd8373bba5351571a01ac14"
) {
    /**
     * Convert data class to hashmap
     */
    fun convertToHashmap(): MutableMap<String, String> {
        val paramsMap: MutableMap<String, String> = HashMap()
        paramsMap["method"] = method
        paramsMap["api_key"] = api_key
        paramsMap["format"] = format
        paramsMap["page"] = page
        paramsMap["tag"] = tag
        paramsMap["limit"] = limit
        paramsMap["artist"] = artist
        return paramsMap
    }
}

enum class LastfmMethod(val method: String) {
    BY_TAG("tag.gettoptracks"),
    BY_CHART("chart.gettoptracks"),
    BY_ARTIST("artist.gettoptracks")
}