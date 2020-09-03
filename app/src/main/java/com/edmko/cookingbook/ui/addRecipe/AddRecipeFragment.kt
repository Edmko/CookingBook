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
import androidx.core.graphics.decodeBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.edmko.cookingbook.CookingApplication.Companion.appContext
import com.edmko.cookingbook.R
import com.edmko.cookingbook.databinding.AddRecipeFragmentBinding
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
    private val viewModel: AddRecipeViewModel by viewModels()
    private val args: AddRecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModelProvider(this).get(AddRecipeViewModel::class.java)
        val root = inflater.inflate(R.layout.add_recipe_fragment, container, false)
        viewDataBinding = AddRecipeFragmentBinding.bind(root).apply {
            this.viewmodel = viewModel
        }

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel.getRecipe(args.recipeId)

        ingredientsList.layoutManager = LinearLayoutManager(context)
        tags_recycler.layoutManager = FlexboxLayoutManager(activity as Context)

        photo.setOnClickListener(this)
        addIngredientButton.setOnClickListener(this)
        save_recipe.setOnClickListener(this)
        addTagButton.setOnClickListener(this)

        viewModel.ingredients.observe(viewLifecycleOwner, Observer {
            ingredientsList.adapter = null
            ingredientsList.adapter =
                IngredientsAdapter(it)
        })
        viewModel.tags.observe(viewLifecycleOwner, Observer {
            tags_recycler.adapter = TagsAdapter(it, this)
        })
        viewModel.image.observe(viewLifecycleOwner, Observer {

            if (it != "") {
                photo.background = null
                Glide.with(requireActivity())
                    .load(it)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                    .into(photo)
            }

        })

        val simpleItemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
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
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(ingredientsList)

        super.onActivityCreated(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                var bitmap: Bitmap? = null

                try {
                    bitmap = if (Build.VERSION.SDK_INT <=28) {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(appContext.contentResolver, resultUri)
                    } else{
                    val source = ImageDecoder.createSource(appContext.contentResolver, resultUri)
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
                viewModel.addIngredientToList(
                    Pair(
                        addIngredient_text.text.toString(),
                        addValue_text.text.toString()
                    )
                )
                addIngredient_text.text?.clear()
                addValue_text.text?.clear()
                activity?.hideKeyboardEx()
            }
            R.id.save_recipe -> {

                val action =
                    AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment(
                        viewModel.saveRecipe()
                    )
                findNavController().navigate(action)
            }
            R.id.addTagButton -> {
                viewModel.addTagToList(
                    add_tag_text.text.toString()
                )
                add_tag_text.text?.clear()
                activity?.hideKeyboardEx()
            }
        }
    }

    override fun onItemClick(pos: Int) {
        viewModel.removeTagFromList(
            pos
        )
    }
}
