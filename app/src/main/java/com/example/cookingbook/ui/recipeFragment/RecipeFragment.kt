package com.example.cookingbook.ui.recipeFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cookingbook.R
import com.example.cookingbook.ui.addRecipe.IngredientsAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.add_recipe_fragment.ingredientsList
import kotlinx.android.synthetic.main.recipe_fragment.*

class RecipeFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recipe_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = RecipeFragmentArgs.fromBundle(requireArguments()).recipeId
        viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        viewModel.getRecipe(args)


        editBtn.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)

        ingredientsList.layoutManager = LinearLayoutManager(context)

        lifecycle.addObserver(youtube_player_view)
        viewModel.recipe.observe(viewLifecycleOwner, Observer {
            recipe_name.text = it.name
            author_tv.text = ("Author: " + it.author)
            notesTv.text = it.notes
            if (it.image != "")
                Glide.with(requireActivity())
                    .load(it.image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                    .into(recipeImage)
            val adapter = IngredientsAdapter(viewModel.recipe.value!!.ingredients)
            ingredientsList.adapter = adapter
            lifecycle.addObserver(youtube_player_view)

            if (it.link != "") {
                youtube_player_view.visibility = View.VISIBLE
                youtube_player_view.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(
                            it.link, viewModel.currentSecondYouTube
                        )

                        super.onReady(youTubePlayer)
                    }

                    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                        viewModel.currentSecondYouTube = second
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
                    RecipeFragmentDirections.actionRecipeFragmentToAddRecipeFragment(viewModel.recipe.value!!.id)
                findNavController().navigate(action)
            }
            R.id.deleteBtn -> {
                val dialog = AlertDialog.Builder(activity)
                dialog.setTitle(getString(R.string.deleteRecipe))
                    .setPositiveButton(getString(R.string.delete)) { _, _ ->
                        viewModel.repository.deleteRecipe(viewModel.recipe.value!!)
                        findNavController().navigate(
                            RecipeFragmentDirections.actionRecipeFragmentToMainFragment()
                        )
                    }.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    }
                    .show()
            }
        }
    }
}

