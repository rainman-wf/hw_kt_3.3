package client.services

import client.exceprions.*
import common.*
import common.RequestWrapper.*
import common.ResponsesWrapper.*
import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    private val service = ChatService
    private val api = ApiService

    @Test(expected = UnregisteredAccount::class)
    fun unregisteredId() {

        api.userId = 10
        service.sendMessage(NewMessage(1, "text"))
    }

    @Test
    fun getUnreadChatsCount() {

        api.userId = 3
        service.sendMessage(NewMessage(1, "Message #1 from User #3"))
        api.userId = 2
        service.sendMessage(NewMessage(1, "Message #1 to User #2"))
        api.userId = 1

        val result = service.getUnreadChatsCount(GetUnreadChatsCount)

        assertEquals((result as IntResponse).int, 2)
    }

    @Test
    fun getUnreadChatsCount_zero() {

        api.userId = 1
        service.sendMessage(NewMessage(5, "text"))
        api.userId = 5
        service.getChatMessages(GetChatMessages(1))

        val result = service.getUnreadChatsCount(GetUnreadChatsCount)

        assertEquals((result as IntResponse).int, 0)
    }

    @Test
    fun getChats() {

        api.userId = 4

        service.sendMessage(NewMessage(1, "Message #1 to User #1"))
        service.sendMessage(NewMessage(2, "Message #1 to User #2"))
        service.sendMessage(NewMessage(3, "Message #1 to User #3"))

        val expect = mapOf(
            (1 to 4) to (service.getChats(GetChats) as MapResponse<*, *>).map[1 to 4],
            (2 to 4) to (service.getChats(GetChats) as MapResponse<*, *>).map[2 to 4],
            (3 to 4) to (service.getChats(GetChats) as MapResponse<*, *>).map[3 to 4]
        )

        val result = (service.getChats(GetChats) as MapResponse<*, *>).map

        assertEquals(expect, result)
    }

    @Test(expected = ChatNotFound::class)
    fun getChats_empty() {
        api.userId = 6
        service.getChats(GetChats)
    }

    @Test
    fun deleteChat() {

        api.userId = 2

        service.sendMessage(NewMessage(3, "To delete"))
        val result = service.deleteChat(DeleteChat(3))

        assertEquals(result, Success)
    }

    @Test(expected = ChatNotFound::class)
    fun deleteChat_chat_not_found() {
        service.deleteChat(DeleteChat(11))
    }

    @Test
    fun createMessage() {

        api.userId = 1

        val newMessage = NewMessage(2, "Message #1")

        val result = service.sendMessage(newMessage)

        val expect = (service.getChatMessages(GetChatMessages(2)) as MapResponse<*, *>).map[0]

        assertEquals(expect, result)
    }

    @Test
    fun getChatMessages() {

        api.userId = 5

        val expect = mapOf(
            0 to service.sendMessage(NewMessage(4, "Message 1")),
            1 to service.sendMessage(NewMessage(4, "Message 2")),
            2 to service.sendMessage(NewMessage(4, "Message 3")),
            3 to service.sendMessage(NewMessage(4, "Message 4"))
        )

        val result = (service.getChatMessages(GetChatMessages(4)) as MapResponse<*, *>).map

        assertEquals(result, expect)
    }

    @Test(expected = ChatNotFound::class)
    fun getChatMessages_chat_not_found() {
        api.userId = 5
        service.getChatMessages(GetChatMessages(50))
    }

    @Test
    fun deleteMessage() {

        api.userId = 1
        service.sendMessage(NewMessage(4, "Message 1"))

        val result = service.deleteMessage(DeleteMessage(4, 0))

        assertEquals(result, Success)
    }

    @Test(expected = ChatNotFound::class)
    fun deleteMessage_chat_not_found() {
        api.userId = 1
        service.deleteMessage(DeleteMessage(50, 0))
    }

    @Test(expected = MessageNotFound::class)
    fun deleteMessage_message_not_found() {
        api.userId = 1
        service.deleteMessage(DeleteMessage(2, 100))
    }

    @Test(expected = AccessDenied::class)
    fun deleteMessage_access_denied() {
        api.userId = 3
        val msg = service.sendMessage(NewMessage(1, "Text")) as Message
        api.userId = 1
        service.deleteMessage(DeleteMessage(3, msg.id))
    }
}