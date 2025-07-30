// Recipe.kt
package com.wesleyaldrich.pancook.model

import android.net.Uri // Important: Make sure this import is present

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<Int>, // For local drawable images
    val videos: List<Uri> = emptyList(), // For video URIs
    val ingredients: List<Ingredient>,
    val steps: List<Instruction>,
    val servings: Int,
    val duration: String,
    var upvoteCount: Int,
    val recipeMaker: String,
    val nutritionFacts: List<NutritionFact>,
    val comments: List<Comment>,
    var isUpvoted: Boolean = false
)