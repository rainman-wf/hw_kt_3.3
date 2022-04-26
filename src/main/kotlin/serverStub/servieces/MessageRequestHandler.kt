package serverStub.servieces

import common.*
import serverStub.methods.MessageRequestImpl
import java.util.*
import common.ResponsesWrapper.*
import serverStub.servieces.Server.user
import common.ErrorCodes.*
import serverStub.servieces.UserService.users

object MessageRequestHandler : MessageRequestImpl<BaseResponse> {

    private val chatService = ChatRequestHandler

    override fun createMessage(newMessage: NewMessage): BaseResponse {

        if (!users.containsKey(newMessage.peerId)) return Failure(USER_NOT_FOUND.code)

        val chat = chatService.getChatById(newMessage.peerId) ?: chatService.createChat(newMessage.peerId)

        val message = Message(chat.counter, user.id, newMessage.text, Date().time)
        message.readBy.add(user.id)
        chat.messages[chat.counter] = message

        val newChat = chat.copy(counter = chat.counter + 1)
        chatService.updateChat(newMessage.peerId, newChat)

        return message
    }

    override fun getChatMessages(chatMessages: GetChatMessages): BaseResponse {
        val chat = chatService.getChatById(chatMessages.peerId) ?: return Failure(CHAT_NOT_FOUND.code)
        if (chatMessages.offset > chat.messages.values.last().id) return Failure(MESSAGE_NOT_FOUND.code)

        val result = chat.messages.values.asSequence()
            .filter { it.id >= chatMessages.offset }
            .take(chatMessages.count)
            .map { it.id to chat.messages[it.id] as Message }
            .toMap()

        result.values.asSequence()
            .filter { !it.readBy.contains(user.id) }
            .forEach { it.readBy.add(user.id) }

        return MapResponse(result)
    }

    override fun deleteMessage(deleteMessage: DeleteMessage): BaseResponse {
        val chat = chatService.getChatById(deleteMessage.peerId) ?: return Failure(CHAT_NOT_FOUND.code)
        val message = chat.messages[deleteMessage.messageId] ?: return Failure(MESSAGE_NOT_FOUND.code)
        if (message.fromId != user.id) return Failure(ACCESS_DENIED.code)
        chat.messages.remove(deleteMessage.messageId)
        if (chat.messages.isEmpty()) chatService.deleteChat(DeleteChat(deleteMessage.peerId))
        return Success
    }

}