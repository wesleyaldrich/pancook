package com.wesleyaldrich.pancook.model

data class Instruction(
    val stepNumber: Int,
    val description: String,
    val timerSeconds: Int = 0,
    val imageRes: Int? = null,
    val videoUrl: String? = null
)