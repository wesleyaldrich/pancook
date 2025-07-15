package com.wesleyaldrich.pancook.model

data class Comment(
    val userName: String,
    val commentText: String,
    val imageUrl: String? = null, // Store URI as String
    val isUpvote: Boolean? = null // Null if no vote, true for upvote, false for downvote
)