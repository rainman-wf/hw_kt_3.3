package client.services

import client.exceprions.AccessDenied
import client.exceprions.ChatNotFound
import client.exceprions.MessageNotFound
import client.exceprions.UserNotFound
import client.methods.ChatIml
import client.methods.MessageImpl
import common.*
import common.RequestWrapper.*
import common.ResponsesWrapper.*
import common.ErrorCodes.*

object ChatService : ChatIml<BaseResponse>, MessageImpl<BaseResponse> {

    private val api = ApiService

    override fun getUnreadChatsCount(unreadChatsCount: GetUnreadChatsCount): BaseResponse {
        return api.execute(unreadChatsCount) as IntResponse
    }

    override fun getChats(getChats: GetChats): BaseResponse {
        val result = api.execute(getChats)
        if (result is Failure && result.code == CHAT_NOT_FOUND.code) throw ChatNotFound()
        return result as MapResponse<*, *>
    }

    override fun deleteChat(deleteChat: DeleteChat): BaseResponse {
        val result = api.execute(deleteChat)
        if (result is Failure && result.code == CHAT_NOT_FOUND.code) throw ChatNotFound()
        return result as Success
    }

    override fun sendMessage(newMessage: NewMessage): BaseResponse {
        val result = api.execute(newMessage)
        if (result is Failure && result.code == USER_NOT_FOUND.code) throw UserNotFound()
        return result as Message
    }

    override fun getChatMessages(chatMessages: GetChatMessages): BaseResponse {
        val result = api.execute(chatMessages)
        if (result is Failure) when (result.code) {
            CHAT_NOT_FOUND.code -> throw ChatNotFound()
        }
        return result as MapResponse<*, *>
    }

    override fun deleteMessage(deleteMessage: DeleteMessage): BaseResponse {
        val result = api.execute(deleteMessage)
        if (result is Failure) when (result.code) {
            CHAT_NOT_FOUND.code -> throw ChatNotFound()
            MESSAGE_NOT_FOUND.code -> throw MessageNotFound()
            ACCESS_DENIED.code -> throw AccessDenied()
        }
        return result as Success
    }

}