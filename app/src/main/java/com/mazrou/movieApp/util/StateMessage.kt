package com.mazrou.movieApp.util


data class StateMessage(val response: Response<Any?>)

data class Response<T>(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

sealed class UIComponentType{
    class Toast: UIComponentType()


    class Dialog: UIComponentType()

    class None: UIComponentType()

}

sealed class MessageType{

    class Success: MessageType()

    class Error: MessageType()

    class Info: MessageType()

    class None: MessageType()
}
interface StateMessageCallback{
    fun removeMessageFromStack()
}
