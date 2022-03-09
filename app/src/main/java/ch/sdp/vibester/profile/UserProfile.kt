package ch.sdp.vibester.profile

data class UserProfile(
    val handle: String = "",
    val username: String = "",
    val image: String = "",
    val totalGames: Int = 0,
    val bestScore: Int = 0,
    val correctSongs: Int = 0,
    val ranking: Int = 0)