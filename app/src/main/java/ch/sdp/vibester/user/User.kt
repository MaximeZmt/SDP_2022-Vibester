package ch.sdp.vibester.user
import java.io.Serializable

data class User(
    var username: String = "",
    var image: String = "",
    var email: String = "",
    var totalGames: Int = 0,
    var bestScore: Int = 0,
    var correctSongs: Int = 0,
    var ranking: Int = 0,
    var uid: String = "",
    var friends: Map<String, Boolean> = mapOf()
    ) : Serializable {}