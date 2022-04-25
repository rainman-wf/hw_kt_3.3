package common

enum class ErrorCodes(val code: Int) {
    INVALID_REQUEST_DATA(0),
    USER_NOT_FOUND(1),
    CHAT_NOT_FOUND(2),
    MESSAGE_NOT_FOUND(3),
    ACCESS_DENIED(4),
    UNREGISTERED_ACCOUNT(5)
}