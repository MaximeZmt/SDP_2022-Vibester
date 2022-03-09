package ch.sdp.vibester.profile

class ProfileDataProvider(userID: String) {
    private lateinit var user: UserProfile
    private val userID = userID

    private val users: List<UserProfile> = listOf(
        UserProfile("user0", "username0","",  5, 8, 34),
        UserProfile("user1", "username1", "", 5, 8, 34),
        UserProfile("user2", "username2", "", 5, 8, 34),
        UserProfile("user3", "username3", "", 5, 8, 34),
        UserProfile("user4", "username4", "", 5, 8, 34),
        UserProfile("user5", "username5", "", 5, 8, 34)
    )
//    private val globalRanking: List<Int>


    fun getUserData():UserProfile{
//         TODO: get data from the Firebase based on id
        return users[userID.toInt()]
    }

    fun getUserRanking(){
        // TODO: get user ranking from the firebase
    }

}