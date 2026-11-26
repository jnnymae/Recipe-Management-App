package com.example.recipemanagementapp.models

sealed class RecipeListItem {
    data class Header(val flavorTitle: FlavorTitle) : RecipeListItem()
    data class RecipeItem(val recipe: Recipe) : RecipeListItem()
}