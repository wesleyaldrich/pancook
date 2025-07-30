package com.wesleyaldrich.pancook.model

data class Instruction(
    val stepNumber: Int,
    val description: String,
    val timerSeconds: Int = 0,
    val images: List<Int> = emptyList(),
    val videoUrl: String? = null
)