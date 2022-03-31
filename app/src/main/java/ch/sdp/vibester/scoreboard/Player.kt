package ch.sdp.vibester.scoreboard

/**
 * Player class for scoreboard
 *
 * @param nik id of the player
 * @param name name of the player
 * @param photo profile photo of the player
 * @param score score of the player
 */
data class Player(
    val nik: Int = 0,
    val name: String = "",
    val photo: String = "",
    val score: Int = 0
)