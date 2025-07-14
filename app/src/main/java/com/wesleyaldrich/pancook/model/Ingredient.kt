package com.wesleyaldrich.pancook.model

data class Ingredient(
//    val id: String,
    val imageRes: Int,
    val name: String,
    val category: String,
    val qty: Int,
    val unitMeasurement: String,
) {
    fun quantityString(): String {
        return "$qty $unitMeasurement"
    }
}