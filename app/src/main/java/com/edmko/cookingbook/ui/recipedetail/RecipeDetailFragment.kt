package com.edmko.cookingbook.ui.recipedetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.R
import com.edmko.cookingbook.base.BaseFragment
import com.edmko.cookingbook.base.utils.injectViewModel
import com.edmko.cookingbook.coredi.App
import com.edmko.cookingbook.databinding.RecipeFragmentBinding
import com.edmko.cookingbook.ui.addrecipe.adapter.IngredientsAdapter
import com.edmko.cookingbook.ui.recipedetail.di.RecipeDetailComponent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.add_recipe_fragment.ingredientsList
import kotlinx.android.synthetic.main.recipe_fragment.*
import javax.inject.Inject

class RecipeDetailFragment : BaseFragment<RecipeDetailViewModel, RecipeFragmentBinding>(),
    View.OnClickListener {

    override val layoutResId: Int = R.layout.recipe_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getViewModel(): RecipeDetailViewModel? = injectViewModel(viewModelFactory)

    override fun injectDependencies() {
        val applicationProvider = (requireActivity().application as App).getApplicationProvider()
        RecipeDetailComponent.build(applicationProvider).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = RecipeDetailFragmentArgs.fromBundle(requireArguments()).recipeId
        getViewModel()?.getRecipe(args)

        editBtn.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)

        ingredientsList.layoutManager = LinearLayoutManager(context)

        lifecycle.addObserver(youtube_player_view)
        getViewModel()?.recipe?.observe(viewLifecycleOwner, Observer {
            recipe_name.text = it.name
            author_tv.text = ("Author: " + it.author)
            notesTv.text = it.notes
            if (it.image != "")
                Glide.with(requireActivity())
                    .load(it.image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                    .into(recipeImage)
            val adapter = IngredientsAdapter(getViewModel()?.recipe?.value!!.ingredients)
            ingredientsList.adapter = adapter
            lifecycle.addObserver(youtube_player_view)

            if (it.link != "") {
                youtube_player_view.visibility = View.VISIBLE
                youtube_player_view.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(
                            it.link, getViewModel()!!.currentSecondYouTube
                        )

                        super.onReady(youTubePlayer)
                    }

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                        Toast.makeText(context, "Bad YouTube Link", Toast.LENGTH_LONG).show()
                        youtube_player_view.visibility = View.GONE
                        super.onError(youTubePlayer, error)
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        getViewModel()?.currentSecondYouTube = second
                        super.onCurrentSecond(youTubePlayer, second)
                    }
                })
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.editBtn -> {
                val action =
                    RecipeDetailFragmentDirections.actionRecipeFragmentToAddRecipeFragment(getViewModel()?.recipe?.value!!.id)
                findNavController().navigate(action)
            }
            R.id.deleteBtn -> {
                val dialog = AlertDialog.Builder(activity)
                dialog.setTitle(getString(R.string.deleteRecipe))
                    .setPositiveButton(getString(R.string.delete)) { _, _ ->
                        getViewModel()?.deleteRecipe(getViewModel()?.recipe?.value!!)
                        findNavController().navigate(
                            RecipeDetailFragmentDirections.actionRecipeFragmentToMainFragment()
                        )
                    }.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    }
                    .show()
            }
        }
    }
}

