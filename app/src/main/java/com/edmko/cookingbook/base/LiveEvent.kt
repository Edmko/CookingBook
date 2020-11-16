package com.edmko.cookingbook.base

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * Send event to observer if did not get the current value.
 * This realization we can use for events like notifications and errors (as sample to show in toasts, snackbars, dialogs)
 * You can always make [LiveEvent] from [androidx.lifecycle.LiveData] use extension [ru.agima.mycars.base.utils.toLiveEvent]
 * Details of this solution https://proandroiddev.com/livedata-with-single-events-2395dea972a8
 * As sample use
 * private val notificationMutableLiveData = MutableLiveData<DialogEvent>()
 * val notificationLiveEvent: LiveData<DialogEvent> = notificationMutableLiveData.toLiveEvent()
 */
class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove<Observer<out Any?>?>(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}