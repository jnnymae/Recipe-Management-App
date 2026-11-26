package com.example.recipemanagementapp

import com.example.recipemanagementapp.adapter.RecipeAdapter
import com.example.recipemanagementapp.models.Flavor
import com.example.recipemanagementapp.models.Recipe
import com.example.recipemanagementapp.models.RecipeListItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecipeAdapterTest {

    private lateinit var adapter: RecipeAdapter

    @Before
    fun setUp() {
        adapter = RecipeAdapter(onRecipeClick = {})
    }

    @Test
    fun initialAdapter_containsTwoHeaders() {
        assertEquals(2, adapter.itemCount)
        val firstItem = adapter.getItem(0)
        val secondItem = adapter.getItem(1)

        assertTrue(firstItem is RecipeListItem.Header)
        assertTrue(secondItem is RecipeListItem.Header)

        assertEquals("Savory", (firstItem as RecipeListItem.Header).flavorTitle.name)
        assertEquals("Sweet", (secondItem as RecipeListItem.Header).flavorTitle.name)
    }

    @Test
    fun addSavoryRecipe_insertsUnderSavoryHeader() {
        val savoryRecipe = Recipe("Pizza", "Cheese Pizza", Flavor.SAVORY)
        adapter.addRecipe(savoryRecipe)

        assertEquals(3, adapter.itemCount)
        val recipeItem = adapter.getItem(1)
        assertTrue(recipeItem is RecipeListItem.RecipeItem)
        assertEquals("Pizza", (recipeItem as RecipeListItem.RecipeItem).recipe.title)

        // Sweet header should now be at position 2
        val sweetHeader = adapter.getItem(2)
        assertTrue(sweetHeader is RecipeListItem.Header)
        assertEquals("Sweet", (sweetHeader as RecipeListItem.Header).flavorTitle.name)
    }

    @Test
    fun addSweetRecipe_appendsUnderSweetHeader() {
        val sweetRecipe = Recipe("Cake", "Chocolate Cake", Flavor.SWEET)
        adapter.addRecipe(sweetRecipe)

        assertEquals(3, adapter.itemCount)
        val recipeItem = adapter.getItem(2)
        assertTrue(recipeItem is RecipeListItem.RecipeItem)
        assertEquals("Cake", (recipeItem as RecipeListItem.RecipeItem).recipe.title)
    }

    @Test
    fun removeRecipe_removesCorrectItem() {
        val savoryRecipe = Recipe("Pizza", "Cheese Pizza", Flavor.SAVORY)
        adapter.addRecipe(savoryRecipe)
        assertEquals(3, adapter.itemCount)

        adapter.removeAt(1)
        assertEquals(2, adapter.itemCount)

        val firstItem = adapter.getItem(0)
        val secondItem = adapter.getItem(1)
        assertEquals("Savory", (firstItem as RecipeListItem.Header).flavorTitle.name)
        assertEquals("Sweet", (secondItem as RecipeListItem.Header).flavorTitle.name)
    }
}