package com.example.recipemanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanagementapp.adapter.RecipeAdapter
import com.example.recipemanagementapp.models.Flavor
import com.example.recipemanagementapp.models.Recipe

class MainActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeTitleEditText: EditText
    private lateinit var recipeDescriptionEditText: EditText
    private lateinit var addSavoryButton: Button
    private lateinit var addSweetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.recipe_book_title)

        val recyclerView = findViewById<RecyclerView>(R.id.main_recipes_list)
        recipeTitleEditText = findViewById(R.id.main_recipe_title)
        recipeDescriptionEditText = findViewById(R.id.main_recipe_description)
        addSavoryButton = findViewById(R.id.main_add_savory_button)
        addSweetButton = findViewById(R.id.main_add_sweet_button)

        recipeAdapter = RecipeAdapter { recipe ->
            showRecipeDialog(recipe)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recipeAdapter

        addSavoryButton.setOnClickListener {
            addRecipe(Flavor.SAVORY)
        }

        addSweetButton.setOnClickListener {
            addRecipe(Flavor.SWEET)
        }

        setupSwipeToDelete(recyclerView)
    }

    private fun addRecipe(flavor: Flavor) {
        val title = recipeTitleEditText.text.toString().trim()
        val description = recipeDescriptionEditText.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_title, Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = Recipe(title = title, description = description, flavor = flavor)
        recipeAdapter.addRecipe(recipe)

        // Clear input form after adding recipe
        recipeTitleEditText.text.clear()
        recipeDescriptionEditText.text.clear()
    }

    private fun showRecipeDialog(recipe: Recipe) {
        val description = recipe.description.ifBlank { getString(R.string.no_description) }
        AlertDialog.Builder(this)
            .setTitle(recipe.title)
            .setMessage(description)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is RecipeAdapter.FlavorTitleViewHolder) {
                    return 0
                }
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    recipeAdapter.removeAt(position)
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }
}