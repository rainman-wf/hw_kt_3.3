package serverStub.servieces

import common.*
import serverStub.methods.ChatRequestImpl
import common.ResponsesWrapper.*
import common.ErrorCodes.*
import serverStub.servieces.Server.user

import kotlin.math.*

object ChatRequestHandler : ChatRequestImpl<BaseResponse> {

    private val chats = mutableMapOf<Pair<Int, Int>, Chat>()

    override fun deleteChat(deleteChat: DeleteChat): BaseResponse {
        val key = chatKey(user.id, deleteChat.peerId)
        chats.remove(key) ?: return Failure(CHAT_NOT_FOUND.code)
        return Success
    }

    override fun getUnreadChatsCount(): BaseResponse {
        val unread = chats
            .getUserChats(user.id)
            .filter { !it.value.messages.values.last().readBy.contains(user.id) }
        return IntResponse(unread.size)
    }

    override fun getChats(): BaseResponse {
        val filtered = chats.getUserChats(user.id)
        if (filtered.isEmpty()) return Failure(CHAT_NOT_FOUND.code)
        return MapResponse(filtered)
    }

    private fun chatKey(sender: Int, peer: Int): Pair<Int, Int> = min(sender, peer) to max(sender, peer)

    private fun <V> Map<Pair<Int, Int>, V>.getUserChats(userId: Int): Map<Pair<Int, Int>, V> =
        this.filterKeys { it.first == userId || it.second == userId }

    fun getChatById(peerId: Int): Chat? = chats[chatKey(user.id, peerId)]

    fun createChat(peerId: Int): Chat {
        val key = chatKey(user.id, peerId)
        val chat: () -> Chat = {
            val newChat = Chat()
            chats[key] = newChat
            newChat
        }
        return chats[key] ?: chat()
    }

    fun updateChat(peerId: Int, chat: Chat) {
        val key = chatKey(user.id, peerId)
        chats[key] = chat
    }
}





