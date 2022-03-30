package ch.sdp.vibester.profile
//import kotlinx.serialization.Serializable
import java.io.Serializable

data class UserProfile (
    var handle: String = "",
    var username: String = "",
    var image: String = "",
    var email: String = "",
    var totalGames: Int = 0,
    var bestScore: Int = 0,
    var correctSongs: Int = 0,
    var ranking: Int = 0): Serializable{}