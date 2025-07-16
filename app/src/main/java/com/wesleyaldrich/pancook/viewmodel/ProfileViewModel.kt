package com.wesleyaldrich.pancook.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wesleyaldrich.pancook.model.ProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val _profileState = MutableStateFlow(
        ProfileData(
            name = "Pancokers",
            email = "pancook@example.com",
            profilePictureResId = null
        )
    )
    val profileState: StateFlow<ProfileData> = _profileState

    fun updateProfilePicture(resId: Int) {
        _profileState.value = _profileState.value.copy(profilePictureResId = resId)
    }

    fun updateName(newName: String) {
        _profileState.value = _profileState.value.copy(name = newName)
    }

    fun updateEmail(newEmail: String) {
        _profileState.value = _profileState.value.copy(email = newEmail)
    }

    fun saveProfile() {
        println("Saved profile: ${_profileState.value}")
    }
}
