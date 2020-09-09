package com.edmko.cookingbook.ui.addRecipe

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.CookingApplication.Companion.appContext
import com.edmko.cookingbook.R
import com.edmko.cookingbook.ViewModelFactory
import com.edmko.cookingbook.databinding.AddRecipeFragmentBinding
import com.edmko.cookingbook.repository.AppRepository
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

class AddRecipeFragment : Fragment(), View.OnClickListener, OnTagClickListener {

    private lateinit var viewDataBinding: AddRecipeFragmentBinding
    private val viewModel by viewModels<AddRecipeViewModel> {
        ViewModelFactory(
            AppRepository(),
            this
        )
    }
    private val args: AddRecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_recipe_fragment, container, false)
        viewDataBinding = AddRecipeFragmentBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel.getRecipe(args.recipeId)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                var bitmap: Bitmap? = null

                try {
                    bitmap = if (Build.VERSION.SDK_INT <= 28) {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(appContext.contentResolver, resultUri)
                    } else {
                        val source =
                            ImageDecoder.createSource(appContext.contentResolver, resultUri)
                        ImageDecoder.decodeBitmap(source)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val contextWrapper = ContextWrapper(appContext)
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
                viewModel.updateImage(
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
            viewModel.removeIngredientFromList(
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
                    viewModel.addIngredientToList(
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
                            viewModel.createRecipe()
                        )
                    activity?.hideKeyboardEx()
                    findNavController().navigate(action)
                } else name_text.error = "Name must have 1 character at least"
            }
            R.id.addTagButton -> {
                if (!add_tag_text.text.isNullOrBlank()) {
                    viewModel.addTagToList(
                        add_tag_text.text.toString()
                    )
                    add_tag_text.text?.clear()
                    activity?.hideKeyboardEx()
                } else add_tag_text.error = "Tag must have 1 character at least"
            }
        }
    }

    override fun onItemClick(pos: Int) {
        viewModel.removeTagFromList(
            pos
        )
    }
}
