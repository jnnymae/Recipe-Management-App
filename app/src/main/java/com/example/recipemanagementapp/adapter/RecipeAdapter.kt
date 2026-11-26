package com.example.recipemanagementapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanagementapp.R
import com.example.recipemanagementapp.models.Flavor
import com.example.recipemanagementapp.models.FlavorTitle
import com.example.recipemanagementapp.models.Recipe
import com.example.recipemanagementapp.models.RecipeListItem

class RecipeAdapter(
    private val items: MutableList<RecipeListItem> = mutableListOf(),
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_FLAVOR_TITLE = 0
        private const val TYPE_RECIPE = 1
    }

    init {
        if (items.isEmpty()) {
            items.add(RecipeListItem.Header(FlavorTitle(Flavor.SAVORY.title)))
            items.add(RecipeListItem.Header(FlavorTitle(Flavor.SWEET.title)))
        }
    }

    class FlavorTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text_flavor_title)

        fun bind(header: RecipeListItem.Header) {
            titleTextView.text = header.flavorTitle.name
        }
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text_recipe_title)

        fun bind(recipeItem: RecipeListItem.RecipeItem, onRecipeClick: (Recipe) -> Unit) {
            titleTextView.text = recipeItem.recipe.title
            itemView.setOnClickListener {
                onRecipeClick(recipeItem.recipe)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecipeListItem.Header -> TYPE_FLAVOR_TITLE
            is RecipeListItem.RecipeItem -> TYPE_RECIPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_FLAVOR_TITLE) {
            val view = inflater.inflate(R.layout.item_flavor_title, parent, false)
            FlavorTitleViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_recipe_title, parent, false)
            RecipeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RecipeListItem.Header -> (holder as FlavorTitleViewHolder).bind(item)
            is RecipeListItem.RecipeItem -> (holder as RecipeViewHolder).bind(item, onRecipeClick)
        }
    }

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): RecipeListItem = items[position]

    fun addRecipe(recipe: Recipe) {
        if (recipe.flavor == Flavor.SAVORY) {
            // Find insertion point right before the SWEET header
            val sweetHeaderIndex = items.indexOfFirst {
                (it is RecipeListItem.Header) && (it.flavorTitle.name == Flavor.SWEET.title)
            }
            val insertIndex = if (sweetHeaderIndex != -1) sweetHeaderIndex else items.size
            items.add(insertIndex, RecipeListItem.RecipeItem(recipe))
            try {
                notifyItemInserted(insertIndex)
            } catch (_: Exception) { }
        } else {
            // SWEET: append to end of list
            items.add(RecipeListItem.RecipeItem(recipe))
            try {
                notifyItemInserted(items.size - 1)
            } catch (_: Exception) { }
        }
    }

    fun removeAt(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            try {
                notifyItemRemoved(position)
            } catch (_: Exception) { }
        }
    }
}