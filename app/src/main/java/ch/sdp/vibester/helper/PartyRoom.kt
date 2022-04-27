package ch.sdp.vibester.helper


class PartyRoom() {
    private lateinit var roomName: String
    private lateinit var userEmails: MutableList<String>

    fun addUserEmail(email: String) {
        userEmails.add(email)
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