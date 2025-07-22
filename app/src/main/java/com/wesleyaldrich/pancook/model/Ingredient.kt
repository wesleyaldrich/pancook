package com.wesleyaldrich.pancook.model

data class Ingredient(
//    val id: String, // Keep this commented if not used
    val imageRes: Int,
    val name: String,
    val category: String,
    val qty: Double,
    val unitMeasurement: String,
) {
    fun quantityString(): String {
        return "%.2f %s".format(qty, unitMeasurement)
    }
}