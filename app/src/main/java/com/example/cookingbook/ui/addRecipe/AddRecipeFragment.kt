package com.example.cookingbook.ui.addRecipe

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.R
import com.example.cookingbook.models.Tags
import com.example.cookingbook.utils.OnTagClickListener
import com.example.cookingbook.utils.hideKeyboardEx
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.add_recipe_fragment.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

class AddRecipeFragment : Fragment(), View.OnClickListener, OnTagClickListener {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewModel: AddRecipeViewModel
    private val args: AddRecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_recipe_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(AddRecipeViewModel::class.java)
        ingredientsList.layoutManager = LinearLayoutManager(context)

        if (viewModel.checkTemp() && args.recipeId.isNotEmpty()) {
            viewModel.getRecipe(args.recipeId, viewLifecycleOwner)
        }
//        val autoCompleteAdapter : ArrayAdapter<String> = ArrayAdapter(activity as Context, R.layout.add_recipe_fragment, R.id.addIngredient_text)
//        add_tag_text.setAdapter(autoCompleteAdapter)
        viewModel.recipe.observe(viewLifecycleOwner, Observer {
            author_text.setText(it.author)
            notes_text.setText(it.notes)
            name_text.setText(it.name)

            ingredientsList.adapter = null
            ingredientsList.adapter =
                IngredientsAdapter(activity as Context, it.ingredients)


            tags_recycler.layoutManager = FlexboxLayoutManager(activity as Context)
            tags_recycler.adapter = TagsAdapter(activity as Context, it.tags, this)

            if (it.image != "") {
                val file = File(it.image)
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                try {
                    val bitmap =
                        BitmapFactory.decodeStream(FileInputStream(file), null, options)
                    photo.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
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
                viewModel.removeIngredientFromList(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(ingredientsList)



        photo.setOnClickListener(this)
        addIngredientButton.setOnClickListener(this)
        save_recipe.setOnClickListener(this)
        addTagButton.setOnClickListener(this)

        super.onActivityCreated(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photo.setImageBitmap(imageBitmap)
            val root =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
            if (!root.isNullOrEmpty()) {
                val imgFile = File(root, UUID.randomUUID().toString() + ".jpg")
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, FileOutputStream(imgFile))
                viewModel.updateImage(imgFile.canonicalPath)
            }

        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.photo -> {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(requireActivity().packageManager).also {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
            R.id.addIngredientButton -> {
                viewModel.addIngredientToList(Pair(addIngredient_text.text.toString(), addValue_text.text.toString()))
                addIngredient_text.text?.clear()
                addValue_text.text?.clear()
                activity?.hideKeyboardEx()
            }
            R.id.save_recipe -> {
                viewModel.saveRecipe(
                    name_text.text.toString(),
                    author_text.text.toString(),
                    notes_text.text.toString()
                )
                val action =
                    AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment(viewModel.recipe.value!!.id)
                findNavController().navigate(action)
            }
            R.id.addTagButton -> {
                viewModel.addTagToList(add_tag_text.text.toString())
                add_tag_text.text?.clear()
                activity?.hideKeyboardEx()
            }
        }
    }

    override fun onItemClick(pos: Int) {
        viewModel.removeTagFromList(pos)
    }

}
