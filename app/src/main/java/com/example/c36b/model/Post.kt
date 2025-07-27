package com.example.c36b.model

import android.net.Uri

data class BookReview(
    val username: String = "",
    val time: String = "",
    val bookTitle: String = "",
    val author: String = "",
    val rating: Float = 0.0f,
    val review: String = "",
    val genre: String = "",
    val tags: List<String> = emptyList(),
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val imageUrl: String? = null,
    val imageUri: Uri? = null
)
