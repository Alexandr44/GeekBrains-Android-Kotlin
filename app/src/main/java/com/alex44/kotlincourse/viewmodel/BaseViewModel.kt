package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<G> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + Job()
    }

    @ExperimentalCoroutinesApi
    private val viewStateChannel = BroadcastChannel<G  >(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    @ExperimentalCoroutinesApi
    fun getViewState() : ReceiveChannel<G> = viewStateChannel.openSubscription()
    fun getErrorChannel() : ReceiveChannel<Throwable> = errorChannel

    protected fun setError(error: Throwable) {
        launch {
            errorChannel.send(error)
        }
    }

    @ExperimentalCoroutinesApi
    protected fun setData(data: G) {
        launch {
            viewStateChannel.send(data)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        viewStateChannel.close()
        errorChannel.close()
        coroutineContext.cancel()
        super.onCleared()
    }
}