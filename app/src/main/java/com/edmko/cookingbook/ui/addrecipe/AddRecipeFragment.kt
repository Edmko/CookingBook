package com.edmko.cookingbook.ui.addrecipe

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.R
import com.edmko.cookingbook.base.BaseFragment
import com.edmko.cookingbook.base.utils.injectViewModel
import com.edmko.cookingbook.coredi.App
import com.edmko.cookingbook.databinding.AddRecipeFragmentBinding
import com.edmko.cookingbook.ui.addrecipe.adapter.IngredientsAdapter
import com.edmko.cookingbook.ui.addrecipe.adapter.TagsAdapter
import com.edmko.cookingbook.ui.addrecipe.di.AddRecipeComponent
import com.edmko.cookingbook.utils.OnTagClickListener
import com.edmko.cookingbook.utils.hideKeyboardEx
import com.google.android.flexbox.FlexboxLayoutManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.add_recipe_fragment.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddRecipeFragment : BaseFragment<AddRecipeViewModel, AddRecipeFragmentBinding>(),
    View.OnClickListener, OnTagClickListener {

    override val layoutResId: Int = R.layout.add_recipe_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getViewModel(): AddRecipeViewModel? = injectViewModel(viewModelFactory)

    override fun injectDependencies() {
        val applicationProvider = (requireActivity().application as App).getApplicationProvider()
        AddRecipeComponent.build(applicationProvider).inject(this)
    }

    private val args: AddRecipeFragmentArgs by navArgs()


    override fun onActivityCreated(savedInstanceState: Bundle?) {

        getViewModel()?.getRecipe(args.recipeId)

        initRecyclers()
        initButtons()
        listenViewModelEvents()

        super.onActivityCreated(savedInstanceState)

    }

    private fun initButtons() {
        photo.setOnClickListener(this)
        addIngredientButton.setOnClickListener(this)
        save_recipe.setOnClickListener(this)
        addTagButton.setOnClickListener(this)
    }

    private fun initRecyclers() {
        ingredientsList.layoutManager = LinearLayoutManager(context)
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(ingredientsList)

        tags_recycler.layoutManager = FlexboxLayoutManager(context)
    }

    private fun listenViewModelEvents() {
        getViewModel()?.let { viewModel ->
            viewModel.ingredients.observe(viewLifecycleOwner, Observer {
                ingredientsList.adapter = null
                ingredientsList.adapter =
                    IngredientsAdapter(it)
            })

            viewModel.tags.observe(viewLifecycleOwner, Observer {
                tags_recycler.adapter = TagsAdapter(it, this)
            })

            viewModel.image.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrBlank()) {
                    photo.background = null
                    Glide.with(requireActivity())
                        .load(it)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                        .into(photo)
                }

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                var bitmap: Bitmap? = null

                try {
                    bitmap = if (Build.VERSION.SDK_INT <= 28) {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().applicationContext.contentResolver,
                            resultUri
                        )
                    } else {
                        val source =
                            ImageDecoder.createSource(
                                requireActivity().applicationContext.contentResolver,
                                resultUri
                            )
                        ImageDecoder.decodeBitmap(source)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val contextWrapper = ContextWrapper(requireActivity().applicationContext)
                val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
                directory.mkdirs()

                val fileName =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fName = "$fileName.jpg"
                val file = File(directory, fName)
                if (file.exists()) file.delete()
                try {
                    val fos = FileOutputStream(file)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val uri = Uri.fromFile(file)

                photo.setImageURI(uri)
                getViewModel()?.updateImage(
                    uri.toString()
                )
            }
    }

    private val simpleItemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            getViewModel()?.removeIngredientFromList(
                viewHolder.adapterPosition
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.photo -> {
                CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(1000, 1000)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .start(activity as Context, this)
            }
            R.id.addIngredientButton -> {
                if (!addIngredient_text.text.isNullOrBlank()) {
                    getViewModel()?.addIngredientToList(
                        Pair(
                            addIngredient_text.text.toString(),
                            addValue_text.text.toString()
                        )
                    )
                    addIngredient_text.text?.clear()
                    addValue_text.text?.clear()
                    activity?.hideKeyboardEx()
                } else addIngredient_text.error = "Ingredient must have 1 character at least"
            }
            R.id.save_recipe -> {
                if (!name_text.text.isNullOrBlank()) {
                    val action =
                        AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment(
                            getViewModel()?.createRecipe()?:""
                        )
                    activity?.hideKeyboardEx()
                    findNavController().navigate(action)
                } else name_text.error = "Name must have 1 character at least"
            }
            R.id.addTagButton -> {
                if (!add_tag_text.text.isNullOrBlank()) {
                    getViewModel()?.addTagToList(
                        add_tag_text.text.toString()
                    )
                    add_tag_text.text?.clear()
                    activity?.hideKeyboardEx()
                } else add_tag_text.error = "Tag must have 1 character at least"
            }
        }
    }

    override fun onItemClick(pos: Int) {
        getViewModel()?.removeTagFromList(
            pos
        )
    }
}
