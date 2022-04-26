package serverStub.methods

import common.DeleteMessage
import common.GetChatMessages
import common.NewMessage

interface MessageRequestImpl<T> {
    fun createMessage(newMessage: NewMessage): T
    fun getChatMessages(chatMessages: GetChatMessages): T
    fun deleteMessage(deleteMessage: DeleteMessage): T
}