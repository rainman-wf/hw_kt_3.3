package serverStub.methods

import common.DeleteChat

interface ChatRequestImpl<T> {
    fun getUnreadChatsCount(): T
    fun getChats(): T
    fun deleteChat(deleteChat: DeleteChat): T
}