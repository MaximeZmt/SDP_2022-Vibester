package ch.sdp.vibester.profile

class ProfileDataProvider(userID: String, users: List<UserProfile> = emptyList(), scoreboard: List<Int> = emptyList()) {
    private lateinit var user: UserProfile
    private val userID:Int = userID.toInt()
    private lateinit var users: List<UserProfile>
    private lateinit var scoreboard: List<Int>

    init {
        setUsers(users)
        setScoreboard(scoreboard)
    }

    /**
     * Hardcoded list will be changed to Firebase.
     * Used for testing.
     */
    private fun setScoreboard(scoreboard: List<Int>){
        if(scoreboard.isEmpty()){
            this.scoreboard = listOf(6,3,4,2,1,5)
        }
        else{this.scoreboard  = scoreboard}
    }

    /**
     * Hardcoded list will be changed to Firebase.
     * Used for testing.
     */
    private fun setUsers(users: List<UserProfile>){
        if(users.isEmpty()){
             this.users =  listOf(
                UserProfile("@lisa", "Lalisa Bon","bit.ly/3IUnyAF",  12, 8, 29),
                UserProfile("@flowergirl", "Viktor Gerard", "bit.ly/3CxcRBR", 3, 15, 324),
                UserProfile("@smartie", "Big Mountain", "bit.ly/3IZl6Ji", 115, 22, 62),
                UserProfile("@swanlake", "Sleeping Beauty", "bit.ly/3tO0k8W", 32, 12, 71),
                UserProfile("@lateevening", "Sleepy Bear", "bit.ly/3IUnyAF", 98, 6, 211),
                UserProfile("@swenguy", "Code nonstop", "bit.ly/3sZhbXm", 64, 14, 111)
            )
        }
        else{this.users  = users}
    }


    /**
     * Combine user data profile and user ranking for profile.
     */
    fun getUserProfileData(): UserProfile{
        user = getUserData()
        user.ranking = getUserRanking()
        return user
    }

    /**
     * Retrieve profile data of user.
     */
    private fun getUserData():UserProfile {
        return users[userID]
    }

    /**
     * Retrieve ranking from the scoreboard.
     */
    private fun getUserRanking(): Int {
        return scoreboard[userID]
    }

}