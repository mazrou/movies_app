package com.mazrou.movieApp.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.mazrou.movieApp.R
import com.mazrou.movieApp.ui.main.MainViewModel
import com.mazrou.movieApp.util.MessageType
import com.mazrou.movieApp.util.Response
import com.mazrou.movieApp.util.StateMessageCallback
import com.mazrou.movieApp.util.UIComponentType

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware, UICommunicationListener {


    override val kodein: Kodein by closestKodein()


    val TAG: String = "AppDebug"
    private var dialogInView: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        hideSoftKeyboard()


    }

    override fun onResponseReceived(
        response: Response<Any?>?,
        stateMessageCallback: StateMessageCallback
    ) {

        response?.let {
            when (response.uiComponentType) {

                is UIComponentType.Toast -> {
                    response.message?.let {
                        displayToast(
                            message = it,
                            stateMessageCallback = stateMessageCallback
                        )
                    }
                }

                is UIComponentType.Dialog -> {
                    displayDialog(
                        response = response,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is UIComponentType.None -> {
                    // This would be a good place to send to your Error Reporting
                    // software of choice (ex: Firebase crash reporting)
                    Log.i(TAG, "onResponseReceived: ${response.message}")
                    stateMessageCallback.removeMessageFromStack()
                }

            }
        }
    }

    private fun displayDialog(
        response: Response<Any?>,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            dialogInView = when (response.messageType) {

                is MessageType.Error -> {
                    displayErrorDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Success -> {
                    displaySuccessDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                is MessageType.Info -> {
                    displayInfoDialog(
                        message = message,
                        stateMessageCallback = stateMessageCallback
                    )
                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }


    private fun displayToast(
        message :String,
        stateMessageCallback : StateMessageCallback
    ){
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
        stateMessageCallback.removeMessageFromStack()
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }


    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    private fun displaySuccessDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_success)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }

    private fun displayInfoDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_info)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }
}