package ch.sdp.vibester.helper


class PartyRoom() {
    private lateinit var roomName: String
    private lateinit var userEmails: MutableList<String>
    private lateinit var roomID: String
//    var gameSongList: MutableList<Pair<String, String>> = mutableListOf()

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

    fun getRoomName(): String {
        return roomName
    }

    fun setRoomName(roomName: String) {
        this.roomName = roomName
    }

    fun setEmailList(userEmails: MutableList<String>) {
        this.userEmails = userEmails
    }

//    fun getSongList(): MutableList<Pair<String, String>> {
//        return gameSongList
//    }
//
//    fun setSongList(gameSongList: MutableList<Pair<String, String>>) {
//        this.gameSongList = gameSongList
//    }
}