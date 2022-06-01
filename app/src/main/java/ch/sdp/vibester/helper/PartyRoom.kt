package ch.sdp.vibester.helper


class PartyRoom() {
    private lateinit var userEmails: MutableList<String>
    private lateinit var roomID: String

    fun addUserEmail(email: String) {
        userEmails.add(email)
    }

    fun getRoomID(): String {
        return roomID
    }

    fun setRoomID(roomID: String) {
        this.roomID = roomID
    }

    fun getEmailList(): MutableList<String> {
        return userEmails
    }

    fun setEmailList(userEmails: MutableList<String>) {
        this.userEmails = userEmails
    }

}