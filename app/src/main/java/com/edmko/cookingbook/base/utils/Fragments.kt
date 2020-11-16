package com.edmko.cookingbook.base.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

inline fun <reified T> Fragment.requireListener(): T = requireListener(T::class.java)

fun <T> Fragment.requireListener(listenerClass: Class<T>): T {
    return findListener(listenerClass)
        ?: throw IllegalStateException("Not parentFragment, neither activity implements listener ${listenerClass.simpleName}")
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.findListener(listenerClass: Class<T>): T? {
    var fragment = this
    while (fragment.parentFragment != null) {
        fragment = fragment.requireParentFragment()
        if ((listenerClass.isInstance(fragment))) {
            return fragment as T
        }
    }
    return if (listenerClass.isInstance(activity)) activity as T? else null
}