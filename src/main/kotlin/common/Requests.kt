package common

abstract class BaseRequest

sealed class RequestWrapper : BaseRequest() {
    object GetUnreadChatsCount : RequestWrapper()
    object GetChats : RequestWrapper()
}
data class DeleteChat(val peerId: Int) : BaseRequest()
data class GetChatMessages(val peerId: Int, val offset: Int = 0, val count: Int = 10) : BaseRequest()
data class NewMessage(val peerId: Int, val text: String) : BaseRequest()
data class DeleteMessage(val peerId: Int, val messageId: Int) : BaseRequest()
