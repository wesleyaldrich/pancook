package com.wesleyaldrich.pancook.model

data class ProfileData(
    val name: String,
    val email: String,
    val profilePictureResId: Int? = null
)
