package com.edmko.cookingbook.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding> : Fragment(),
    CoroutineScope {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    lateinit var binding: BINDING

    protected abstract fun getViewModel(): VIEW_MODEL?

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    protected abstract fun injectDependencies()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }


}