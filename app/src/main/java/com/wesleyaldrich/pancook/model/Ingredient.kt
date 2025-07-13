package com.wesleyaldrich.pancook.model

data class Ingredient(
//    val id: String,
    val name: String,
    val qty: Int,
    val unitMeasurement: String,
) {
    fun quantityString(): String {
        return "$qty $unitMeasurement"
    }
}