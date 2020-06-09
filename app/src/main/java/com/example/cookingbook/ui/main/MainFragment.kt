package com.example.cookingbook.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.R
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRecipeFragment)
        }
        preview_vertical.layoutManager = LinearLayoutManager(context)
        viewModel.recipeList.observe(viewLifecycleOwner, Observer {
            if (requireActivity().resources.configuration.orientation == 1) {
                preview_vertical.adapter = RecipeAdapter(
                    activity as Context, it, preview,
                    true
                )
                search_text.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        (preview_vertical.adapter as RecipeAdapter).filter.filter(s)
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }
                })
            } else {
                preview.adapter = InfiniteScrollAdapter.wrap(
                    RecipeAdapter(
                        activity as Context,
                        viewModel.recipeList.value!!,
                        preview, false
                    )
                )
                preview.viewTreeObserver.addOnGlobalLayoutListener(listener)
            }
        })
    }

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (preview.width > 0 && preview.height > 0) {
                preview.adapter = null
                viewModel.recipeList.observe(viewLifecycleOwner, Observer {
                    preview.adapter = InfiniteScrollAdapter.wrap(
                        RecipeAdapter(
                            activity as Context,
                            viewModel.recipeList.value!!,
                            preview, false
                        )
                    )
                    preview.setItemTransformer(ScaleTransformer.Builder().setMinScale(0.5f).build())
                    preview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                )
            }
        }
    }


}