package client.methods

import common.DeleteMessage
import common.GetChatMessages
import common.NewMessage

interface MessageImpl<T> {
    fun sendMessage(newMessage: NewMessage) : T
    fun getChatMessages(chatMessages: GetChatMessages) : T
    fun deleteMessage(deleteMessage: DeleteMessage): T
}