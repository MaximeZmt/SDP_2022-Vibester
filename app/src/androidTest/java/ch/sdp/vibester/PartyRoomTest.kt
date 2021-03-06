package ch.sdp.vibester

import ch.sdp.vibester.helper.PartyRoom
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class PartyRoomTest {
    @Test
    fun correctSetEmailList() {
        val emailList = mutableListOf("email1, email2")
        val testPartyRoom = PartyRoom()

        testPartyRoom.setEmailList(emailList)

        for(email in emailList) {
            assert(testPartyRoom.getEmailList().contains(email))
        }
    }

    @Test
    fun correctAddEmail() {
        var emailList = mutableListOf("email1", "email2")
        val testPartyRoom = PartyRoom()

        testPartyRoom.setEmailList(emailList)
        testPartyRoom.addUserEmail("email3")
        emailList = mutableListOf("email1", "email2", "email3")

        for(email in emailList) {
            assert(testPartyRoom.getEmailList().contains(email))
        }
    }

    @Test
    fun correctSetRoomID() {
        val roomID = "testRoomID"
        val testPartyRoom = PartyRoom()

        testPartyRoom.setRoomID(roomID)

        assertEquals(testPartyRoom.getRoomID(), roomID)
    }
}