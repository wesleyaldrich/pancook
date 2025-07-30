// MediaItem.kt
package com.wesleyaldrich.pancook.model

import android.net.Uri

data class MediaItem(
    val type: MediaType, // This refers to the MediaType enum
    val resourceId: Int? = null, // For image drawables
    val uri: Uri? = null // For video URIs
)