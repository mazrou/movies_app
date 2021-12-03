package com.mazrou.movieApp.ui.main

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mazrou.movieApp.R
import com.mazrou.movieApp.ui.BaseActivity
import com.mazrou.movieApp.util.StateMessageCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.generic.instance
@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {

    private val viewModel : MainViewModel by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        viewModel.stateMessage.observe(this, { stateMessage ->
            stateMessage?.let {
                onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    override fun displayProgressBar(isLoading: Boolean, useDialog: Boolean) {

    }
}