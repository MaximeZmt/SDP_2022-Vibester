package ch.sdp.vibester.helper


class PartyRoom() {
    private lateinit var roomName: String
    private lateinit var userEmails: MutableList<String>
    private lateinit var roomID: String
    private var userCount = 0

    fun addUserEmail(email: String) {
        userEmails.add(email)
    }

    fun getRoomID(): String {
        return roomID
    }

    fun setRoomID(roomID: String) {
        this.roomID = roomID
    }

    fun getUserCount(): Int {
        return userCount
    }

    fun setUserCount(userCount: Int) {
        this.userCount = userCount
    }

    fun getEmailList(): MutableList<String> {
        return userEmails
    }

    fun getRoomName(): String {
        return roomName
    }

    fun setRoomName(roomName: String) {
        this.roomName = roomName
    }

    fun setEmailList(userEmails: MutableList<String>) {
        this.userEmails = userEmails
    }
}