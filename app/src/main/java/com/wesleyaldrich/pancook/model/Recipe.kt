package com.wesleyaldrich.pancook.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val image: Int,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val servings: Int
)