package common

abstract class BaseResponse

data class Chat(
    val counter: Int = 0,
    val messages: MutableMap<Int, Message> = mutableMapOf(),
) : BaseResponse()

data class Message(
    val id: Int,
    val fromId: Int,
    val text: String,
    val date: Long,
    val readBy: MutableList<Int> = mutableListOf()
) : BaseResponse()

sealed class ResponsesWrapper : BaseResponse() {
    data class MapResponse<K, V : BaseResponse>(val map: Map<K, V>) : ResponsesWrapper()
    data class IntResponse(val int: Int) : ResponsesWrapper()
    data class Failure(val code: Int) : ResponsesWrapper()
    object Success : ResponsesWrapper()
}

