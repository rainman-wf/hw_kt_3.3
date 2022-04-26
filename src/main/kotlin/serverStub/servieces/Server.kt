package serverStub.servieces

import common.*
import serverStub.methods.CallbackImpl
import common.ResponsesWrapper.*
import common.RequestWrapper.*
import common.ErrorCodes.*

object Server : CallbackImpl<BaseRequest, BaseResponse> {

    private val chatService = ChatRequestHandler
    private val messageService = MessageRequestHandler

    private var senderId: Int = 0
    lateinit var user: User

    override fun callBack(senderId: Int, request: BaseRequest): BaseResponse {

        user = UserService.users[senderId] ?: return Failure(UNREGISTERED_ACCOUNT.code)

        this.senderId = senderId

        return when (request) {
            is GetUnreadChatsCount -> chatService.getUnreadChatsCount()
            is GetChats -> chatService.getChats()
            is DeleteChat -> chatService.deleteChat(request)
            is NewMessage -> messageService.createMessage(request)
            is GetChatMessages -> messageService.getChatMessages(request)
            is DeleteMessage -> messageService.deleteMessage(request)
            else -> Failure(INVALID_REQUEST_DATA.code)
        }
    }
}

