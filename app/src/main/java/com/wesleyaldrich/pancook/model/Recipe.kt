package com.wesleyaldrich.pancook.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val image: Int,
    val ingredients: List<Ingredient>,
    val steps: List<Instruction>,
    val servings: Int,
    val duration: String,
    val upvoteCount: Int = 0,
    val recipeMaker: String,
    val nutritionFacts: List<NutritionFact>,
    val comments: List<Comment>
)