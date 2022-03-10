package ch.sdp.vibester.profile

class ProfileDataProvider(userID: String, users: List<UserProfile> = emptyList()) {
    private lateinit var user: UserProfile
    private val userID:Int = userID.toInt()
    private lateinit var users: List<UserProfile>

    init {
        setUsers(users)
    }

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


    private val ranking: List<Int> = listOf(6,3,4,2,1,5)

    fun getUserData():UserProfile {
        user = users[userID]
        user.ranking = getUserRanking()
        return user
    }

    private fun getUserRanking(): Int {
        return ranking[userID]
    }

}