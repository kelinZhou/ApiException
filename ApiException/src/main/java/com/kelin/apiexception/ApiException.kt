package com.kelin.apiexception

import android.content.Context
import androidx.annotation.StringRes

class ApiException(
    val errCode: Int,
    private val msg: String? = null,
    throwable: Throwable? = null
) : Exception(msg ?: throwable?.message) {

    companion object {

        private var context: Context? = null

        fun init(context: Context) {
            this.context = context.applicationContext
        }

        internal fun getContext(): Context {
            return context
                ?: throw NullPointerException("You must call the ApiException.init() Method before use the ApiException")
        }
    }


    constructor(errCode: Int, throwable: Throwable) : this(errCode, throwable.message, throwable)

    constructor(errCode: Int, msg: String) : this(errCode, msg, null)

    constructor(error: Error) : this(error.code, error.msg)

    constructor(error: Error, throwable: Throwable) : this(error.code, error.msg, throwable)

    val displayMessage: String
        get() = toString()

    override fun toString() = msg ?: getString(R.string.unknown_failed)

    fun isUserInfoDisabled(): Boolean {
        return errCode == Error.USER_INFO_DISABLED.code
    }

    fun isLoggedOut(): Boolean {
        return errCode == Error.LOGGED_OUT.code
    }

    fun isHttpPermissionError(): Boolean {
        return errCode == Error.TOKEN_INVALID.code || errCode == Error.PARSER_USER_FAILED.code
    }

    fun isServerError(): Boolean {
        return errCode == Error.SERVICE_ERROR.code || errCode == Error.DEADLINE_EXCEEDED.code
    }

    fun isHttpRequestError(httpCode: Int): Boolean {
        return httpCode != 0
    }

    fun isNetworkError(): Boolean {
        return errCode == Error.NETWORK_UNAVAILABLE.code || errCode == Error.NETWORK_UNAVAILABLE.code
    }

    /**
     * 1001 比较特殊，是专门用来捕获没有预料的异常的，由于没有预料到所以这里叫未知异常。
     *
     * 所有7开头的错误都是在检测到用户有一些不正常的操作时给出的友好提示。
     *
     * 所有8开头的异常都属于自定义异常，报错后端定义的自定义异常。
     *
     * 所有9开头的异常都属于服务器端的异常，通常情况下这些异常都是因为后端的代码错误才导致的。
     *
     * 所有6开头的错误都属于代码错误，是可以避免的。这些错误都是提醒给程序员看的。
     */
    enum class Error(val code: Int, val msg: String) {
        /**
         * 所有未知的错误。
         */
        UNKNOWN_ERROR(1001, getString(R.string.unknown_error)),

        /**
         * 当用户进行某项操作失败次数过多是抛出改异常。
         */
        FAIL_TOO_MUCH(7001, getString(R.string.failed_too_much)),

        /**
         * 当用户进行某项操作过于频繁时或者服务器压力过大时抛出该异常。
         */
        TOO_BUSY(7002, getString(R.string.too_busy)),

        /**
         * 没有绑定手机。
         */
        NO_ACCOUNT(8000, getString(R.string.no_account)),

        /**
         * 网络不可用。
         */
        NETWORK_UNAVAILABLE(8001, getString(R.string.network_unavailable)),

        /**
         * 网络错误。
         */
        NETWORK_ERROR(8002, getString(R.string.network_error)),

        /**
         * 服务器异常
         */
        SERVICE_ERROR(9001, getString(R.string.service_error)),

        /**
         * 服务器无响应
         */
        DEADLINE_EXCEEDED(9002, getString(R.string.service_deadline)),

        /**
         * 无法连接服务器
         */
        SOCKET_EXCEPTION(9003, getString(R.string.connect_timeout)),

        /**
         * 数据解析异常。解析数据时没有获取到data或者result信息,获取解析JSON数据错误时都会抛出改异常。
         */
        RESULT_ERROR(9003, getString(R.string.data_error)),

        /**
         * 用户身份解析失败。
         */
        PARSER_USER_FAILED(40005, getString(R.string.parser_user_failed)),

        /**
         * Token无效。
         */
        TOKEN_INVALID(40003, getString(R.string.token_invalid)),

        /**
         * 账号已注销。
         */
        LOGGED_OUT(40015, getString(R.string.logged_out)),

        /**
         * 用户信息已过期。
         */
        USER_INFO_DISABLED(50000, getString(R.string.user_info_disabled)),

        /**
         * 当调用后台API缺少必要参数是抛出该错误。
         */
        ARGUMENT_ERROR(-99, getString(R.string.argument_error));
    }
}

private fun getString(@StringRes res: Int): String {
    return ApiException.getContext().getString(res)
}