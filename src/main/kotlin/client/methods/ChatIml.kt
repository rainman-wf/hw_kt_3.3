package client.methods

import common.*
import common.RequestWrapper.*

interface ChatIml<T> {
    fun getUnreadChatsCount(unreadChatsCount: GetUnreadChatsCount): T
    fun getChats(getChats: GetChats): T
    fun deleteChat(deleteChat: DeleteChat): T
}