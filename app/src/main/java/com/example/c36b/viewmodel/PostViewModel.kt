package com.example.c36b.viewmodel

import androidx.lifecycle.ViewModel
import com.example.c36b.model.BookReview
import com.example.c36b.repository.PostRepository
import android.net.Uri

class PostViewModel(private val repo: PostRepository) : ViewModel() {
    fun createBookReview(review: BookReview, callback: (Boolean, String) -> Unit) {
        repo.createBookReview(review, callback)
    }

    fun getAllBookReviews(callback: (List<BookReview>?, String?) -> Unit) {
        repo.getAllBookReviews(callback)
    }

    fun getBookReviewById(reviewId: String, callback: (BookReview?, String?) -> Unit) {
        repo.getBookReviewById(reviewId, callback)
    }

    fun updateBookReview(reviewId: String, review: BookReview, callback: (Boolean, String) -> Unit) {
        repo.updateBookReview(reviewId, review, callback)
    }

    fun deleteBookReview(reviewId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteBookReview(reviewId, callback)
    }

    fun getBookReviewsByUser(username: String, callback: (List<BookReview>?, String?) -> Unit) {
        repo.getBookReviewsByUser(username, callback)
    }

    fun getBookReviewsByGenre(genre: String, callback: (List<BookReview>?, String?) -> Unit) {
        repo.getBookReviewsByGenre(genre, callback)
    }

    fun getBookReviewsByAuthor(author: String, callback: (List<BookReview>?, String?) -> Unit) {
        repo.getBookReviewsByAuthor(author, callback)
    }

    fun uploadImageToCloudinary(context: android.content.Context, imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        if (repo is com.example.c36b.repository.PostRepositoryImpl) {
            repo.uploadImageToCloudinary(context, imageUri, onResult)
        } else {
            onResult(false, "Upload not supported by this repository implementation.")
        }
    }
}
