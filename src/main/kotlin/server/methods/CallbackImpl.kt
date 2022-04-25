package server.methods

interface CallbackImpl<T, R> {
    fun callBack(senderId: Int, request: T): R
}