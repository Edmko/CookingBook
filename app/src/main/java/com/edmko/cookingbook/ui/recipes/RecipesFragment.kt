package com.edmko.cookingbook.ui.recipes

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.edmko.cookingbook.R
import com.edmko.cookingbook.base.BaseFragment
import com.edmko.cookingbook.base.utils.injectViewModel
import com.edmko.cookingbook.coredi.App
import com.edmko.cookingbook.databinding.MainFragmentBinding
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.ui.recipes.adapter.RecipeAdapter
import com.edmko.cookingbook.ui.recipes.di.RecipesComponent
import com.edmko.cookingbook.utils.OnItemClickListener
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class RecipesFragment : BaseFragment<RecipesViewModel, MainFragmentBinding>(), OnItemClickListener {

    override val layoutResId: Int = R.layout.main_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewModel(): RecipesViewModel? = injectViewModel(viewModelFactory)

    override fun injectDependencies() {
        val applicationProvider = (requireActivity().application as App).getApplicationProvider()
        RecipesComponent.build(applicationProvider).inject(this)
    }

    lateinit var adapter: RecipeAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRecipeFragment)
        }
        initRecyclerView()
        listenViewModel()
        search_text.doAfterTextChanged { editable -> adapter.filter.filter(editable) }

    }

    override fun onItemClick(id: String) {
        val action = RecipesFragmentDirections.actionMainFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        adapter = RecipeAdapter(this)
        recyclerView_vertical.adapter = adapter
    }

    private fun updateDataList(list: List<Recipe>) {
        adapter.setData(list)
    }

    private fun listenViewModel() {
        getViewModel()?.recipeList?.observe(viewLifecycleOwner, Observer { recipeList ->
            recyclerView_vertical.viewTreeObserver.addOnGlobalLayoutListener(verticalListener)
            updateDataList(recipeList)
        })

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
}