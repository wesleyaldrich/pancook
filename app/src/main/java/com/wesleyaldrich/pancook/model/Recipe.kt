// Recipe.kt
package com.wesleyaldrich.pancook.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<Int>, // Changed to List<Int> for multiple images
    val ingredients: List<Ingredient>,
    val steps: List<Instruction>,
    val servings: Int,
    val duration: String,
    var upvoteCount: Int, // Made upvoteCount mutable
    val recipeMaker: String,
    val nutritionFacts: List<NutritionFact>,
    val comments: List<Comment>
)