package com.kelin.apiexception

class ApiException(
    val errCode: Int,
    private val msg: String? = null,
    throwable: Throwable? = null
) : Exception(msg ?: throwable?.message) {

    constructor(errCode: Int, throwable: Throwable) : this(errCode, throwable.message, throwable)

    constructor(errCode: Int, msg: String) : this(errCode, msg, null)

    constructor(error: ApiError) : this(error.code, error.msg)

    constructor(error: ApiError, throwable: Throwable) : this(error.code, error.msg, throwable)

    val displayMessage: String
        get() = toString()

    override fun toString() = msg ?: "Unknown Exception"


    /**
     * 定义Api相关的自定义错误规范。
     */
    interface ApiError {
        val code: Int
        val msg: String
    }
}