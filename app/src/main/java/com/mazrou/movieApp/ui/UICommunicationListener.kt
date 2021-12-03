package com.mazrou.movieApp.ui

import com.mazrou.movieApp.util.Response
import com.mazrou.movieApp.util.StateMessageCallback

/**
 * @author Mazrou Ayoub
 */
interface UICommunicationListener {
    fun onResponseReceived(
        response: Response<Any?>?,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean, useDialog: Boolean = false)


    fun hideSoftKeyboard()

}