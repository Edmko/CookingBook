package com.edmko.cookingbook.ui.recipes

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.edmko.cookingbook.R
import com.edmko.cookingbook.base.BaseFragment
import com.edmko.cookingbook.base.utils.injectViewModel
import com.edmko.cookingbook.coredi.App
import com.edmko.cookingbook.databinding.MainFragmentBinding
import com.edmko.cookingbook.ui.recipes.adapter.RecipeAdapter
import com.edmko.cookingbook.ui.recipes.di.RecipesComponent
import com.edmko.cookingbook.utils.afterMeasured
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class RecipesFragment : BaseFragment<RecipesViewModel, MainFragmentBinding>() {

    override val layoutResId: Int = R.layout.main_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewModel(): RecipesViewModel = injectViewModel(viewModelFactory)

    override fun injectDependencies() {
        val applicationProvider = (requireActivity().application as App).getApplicationProvider()
        RecipesComponent.build(applicationProvider).inject(this)
    }

    override fun setupView(view: View) {
        initRecyclerView()
        listenViewModel()
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addRecipeFragment)
        }
        view.findViewById<TextInputEditText>(R.id.search_text)
            .doAfterTextChanged { editable -> recipeAdapter.filter(editable) }
    }

    override fun setBinding(root: View) {
        binding = MainFragmentBinding.bind(root).apply {
            viewmodel = getViewModel()
        }
    }

    lateinit var recipeAdapter: RecipeAdapter

    private fun initRecyclerView() {
        recipeAdapter = RecipeAdapter()
        rvRecipes.adapter = recipeAdapter

        recipeAdapter.onItemClick = { recipeId ->
            navigateToRecipeFragment(recipeId)
        }

        rvRecipes.afterMeasured {
            translationY = -appBar.height.toFloat()
            layoutParams.height =
                height + appBar.height
        }
    }

    private fun navigateToRecipeFragment(id: String) {
        val action = RecipesFragmentDirections.actionMainFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }

    private fun listenViewModel() {
        getViewModel().recipeList.observe(viewLifecycleOwner) { recipeList ->
            recipeAdapter.setData(recipeList)
        }

    }
}