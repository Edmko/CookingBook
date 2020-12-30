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
import androidx.fragment.app.viewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding> : Fragment(),
    CoroutineScope {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    open lateinit var binding: BINDING

    protected abstract fun getViewModel(): VIEW_MODEL?

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    protected abstract fun injectDependencies()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }
    protected abstract fun setupView(view:View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(layoutResId, container, false)
        setBinding(root)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    protected abstract fun setBinding(root: View)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }
}