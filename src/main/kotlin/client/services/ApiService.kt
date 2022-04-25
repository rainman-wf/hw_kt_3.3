package client.services

import client.exceprions.UnregisteredAccount
import client.methods.Executable
import common.*
import common.ErrorCodes.*
import common.ResponsesWrapper.*
import server.servieces.Server


object ApiService : Executable<BaseRequest, BaseResponse> {

    var userId = 0

    override fun execute(r: BaseRequest): BaseResponse {
        val result = Server.callBack(userId, r)
        if (result is Failure && result.code == UNREGISTERED_ACCOUNT.code) throw UnregisteredAccount()
        return result
    }

}