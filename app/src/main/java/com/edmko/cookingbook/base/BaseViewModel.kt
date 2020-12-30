package com.edmko.cookingbook.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edmko.cookingbook.base.utils.toLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel(), CoroutineScope {

    private val scopeJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext = scopeJob

    private val _eventLiveEvent = MutableLiveData<BaseEvent>()
    val eventLiveEvent = _eventLiveEvent.toLiveEvent()
}