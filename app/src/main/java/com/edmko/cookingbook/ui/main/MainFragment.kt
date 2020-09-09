package com.edmko.cookingbook.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.edmko.cookingbook.R
import com.edmko.cookingbook.ViewModelFactory
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.repository.AppRepository
import com.edmko.cookingbook.utils.OnItemClickListener
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), OnItemClickListener {

    lateinit var adapter: RecipeAdapter
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory(AppRepository(), this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRecipeFragment)
        }
        initRecyclerView()
        listenViewModel()
        search_text.addTextChangedListener(textSearchListener)

    }

    override fun onItemClick(id: String) {
        val action =  MainFragmentDirections.actionMainFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        if (requireActivity().resources.configuration.orientation == 1) {
            adapter = RecipeAdapter(true,this)
            recyclerView_vertical.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@MainFragment.adapter
            }

        } else {
            adapter = RecipeAdapter(false,this)
            recyclerView_horizontal.apply {
                adapter = InfiniteScrollAdapter.wrap(this@MainFragment.adapter)

            }
        }
    }

    private fun updateDataList(list: List<Recipe>) {
        adapter.setData(list)
    }

    private fun listenViewModel() {
        viewModel.recipeList.observe(viewLifecycleOwner, Observer {
            if (requireActivity().resources.configuration.orientation == 1) {
                recyclerView_vertical.viewTreeObserver.addOnGlobalLayoutListener(verticalListener)
            } else {
                recyclerView_horizontal.viewTreeObserver.addOnGlobalLayoutListener(
                    horizontalListener
                )
            }
            updateDataList(it)
        })

    }

    private val horizontalListener = object : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            if (recyclerView_horizontal.width > 0 && recyclerView_horizontal.height > 0) {
                recyclerView_horizontal.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.recipeList.observe(viewLifecycleOwner, Observer {
                    recyclerView_horizontal.adapter = null
                    recyclerView_horizontal.setItemTransformer(
                        ScaleTransformer.Builder().setMinScale(0.5f).build()
                    )
                    initRecyclerView()
                    updateDataList(it)
                })
            }
        }
    }
    private val verticalListener = object : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            recyclerView_vertical.viewTreeObserver.removeOnGlobalLayoutListener(this)

            recyclerView_vertical.apply {
                translationY = -appBar.height.toFloat()
                layoutParams.height =
                    height + appBar.height
            }

        }
    }
    private val textSearchListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            adapter.filter.filter(s)
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }
    }
}