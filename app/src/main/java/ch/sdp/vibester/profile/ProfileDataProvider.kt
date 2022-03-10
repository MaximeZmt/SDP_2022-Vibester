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
                UserProfile("user0", "username0","bit.ly/3IUnyAF",  5, 8, 34),
                UserProfile("user1", "username1", "bit.ly/3CxcRBR", 5, 8, 34),
                UserProfile("user2", "username2", "bit.ly/3IZl6Ji", 5, 8, 34),
                UserProfile("user3", "username3", "bit.ly/3tO0k8W", 5, 8, 34),
                UserProfile("user4", "username4", "bit.ly/3IUnyAF", 5, 8, 34),
                UserProfile("user5", "username5", "bit.ly/3sZhbXm", 5, 8, 34)
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