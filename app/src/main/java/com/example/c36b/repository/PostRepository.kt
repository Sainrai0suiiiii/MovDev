package com.example.c36b.repository

import com.example.c36b.model.BookReview

interface PostRepository {
    fun createBookReview(review: BookReview, callback: (Boolean, String) -> Unit)
    fun getAllBookReviews(callback: (List<BookReview>?, String?) -> Unit)
    fun getBookReviewById(reviewId: String, callback: (BookReview?, String?) -> Unit)
    fun updateBookReview(reviewId: String, review: BookReview, callback: (Boolean, String) -> Unit)
    fun deleteBookReview(reviewId: String, callback: (Boolean, String) -> Unit)
    fun getBookReviewsByUser(username: String, callback: (List<BookReview>?, String?) -> Unit)
    fun getBookReviewsByGenre(genre: String, callback: (List<BookReview>?, String?) -> Unit)
    fun getBookReviewsByAuthor(author: String, callback: (List<BookReview>?, String?) -> Unit)
}

