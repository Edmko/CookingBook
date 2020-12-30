package com.edmko.cookingbook.ui.addrecipe

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.edmko.cookingbook.utils.hideKeyboardEx
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
    View.OnClickListener {


    override val layoutResId: Int = R.layout.add_recipe_fragment

    override fun setBinding(root: View) {
        binding = AddRecipeFragmentBinding.bind(root).apply {
            viewmodel = getViewModel()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun getViewModel(): AddRecipeViewModel = injectViewModel(viewModelFactory)

    override fun injectDependencies() {
        val applicationProvider = (requireActivity().application as App).getApplicationProvider()
        AddRecipeComponent.build(applicationProvider).inject(this)
    }

    private val args: AddRecipeFragmentArgs by navArgs()

    lateinit var ingredientsAdapter: IngredientsAdapter
    lateinit var tagsAdapter: TagsAdapter

    override fun setupView(view: View) {
        getViewModel().getRecipe(args.recipeId)
        initRecyclers()
        initButtons()
        listenViewModelEvents()
    }

    private fun initButtons() {
        imgPhoto.setOnClickListener(this)
        btnAddIngredient.setOnClickListener(this)
        btnSaveRecipe.setOnClickListener(this)
        btnTag.setOnClickListener(this)
    }

    private fun initRecyclers() {
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(ingredientsList)
        ingredientsAdapter = IngredientsAdapter()
        ingredientsList.adapter = ingredientsAdapter
        tagsAdapter = TagsAdapter()
        tagsAdapter.onTagClicks = { position ->
            getViewModel().removeTagFromList(position)
        }
        rvTags.adapter = tagsAdapter
    }

    private fun listenViewModelEvents() {
        getViewModel().let { viewModel ->
            viewModel.ingredients.observe(viewLifecycleOwner) {
                ingredientsAdapter.setData(it)
            }

            viewModel.tags.observe(viewLifecycleOwner) {
                tagsAdapter.setData(it)
            }

            viewModel.image.observe(viewLifecycleOwner) {
                if (it.isNullOrBlank().not()) {
                    imgPhoto.background = null
                    Glide.with(requireActivity())
                        .load(it)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                        .into(imgPhoto)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
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

            imgPhoto.setImageURI(uri)
            getViewModel().updateImage(
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
            getViewModel().removeIngredientFromList(
                viewHolder.adapterPosition
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgPhoto -> onPhotoClick()
            R.id.btnAddIngredient -> addIngredientClick()
            R.id.btnSaveRecipe -> saveRecipeClick()
            R.id.btnTag -> addTagClick()
        }
    }

    private fun addTagClick() {
        if (evTag.text?.isNotBlank() == true) {
            getViewModel().addTagToList(
                evTag.text.toString().toLowerCase(Locale.getDefault())
            )
            evTag.text?.clear()
            activity?.hideKeyboardEx()
        } else evTag.error = getString(R.string.tag_error)
    }

    private fun saveRecipeClick() {
        if (evName.text?.isNotBlank() == true) {
            val action =
                AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment(
                    getViewModel().createRecipe()
                )
            activity?.hideKeyboardEx()
            findNavController().navigate(action)
        } else evName.error = getString(R.string.name_error)
    }

    private fun addIngredientClick() {
        if (evIngredient.text?.isNotBlank() == true) {
            getViewModel().addIngredientToList(
                evIngredient.text.toString() to evValue.text.toString()
            )
            evIngredient.text?.clear()
            evValue.text?.clear()
            activity?.hideKeyboardEx()
        } else evIngredient.error = getString(R.string.ingredient_error)
    }

    private fun onPhotoClick() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setMinCropResultSize(1000, 1000)
            .setGuidelines(CropImageView.Guidelines.OFF)
            .start(activity as Context, this)
    }
}
