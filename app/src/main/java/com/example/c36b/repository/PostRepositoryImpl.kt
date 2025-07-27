package com.example.c36b.repository

import android.content.Context
import android.net.Uri
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.c36b.model.BookReview
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.InputStream
import java.util.concurrent.Executors
import android.os.Handler


class PostRepositoryImpl : PostRepository {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.reference.child("bookReviews")

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "doayfq3xr",
            "api_key" to "652925374972233",
            "api_secret" to "lGt49TnaPIIrdJ_8tG_EJ8AHY1E"
        )
    )

    fun createBookReview(context: Context, review: BookReview, callback: (Boolean, String) -> Unit) {
        // Upload image to Cloudinary first
        if (review.imageUri == null) {
            callback(false, "Book cover image is required")
            return
        }
        uploadImageToCloudinary(context, review.imageUri!!, onResult = { success, imageUrl ->
            if (success && imageUrl != null) {
                // Set the imageUrl in the review object, but remove imageUri for Firebase
                val reviewWithImageUrl = review.copy(imageUrl = imageUrl, imageUri = null)
                // Generate a new key for the review
                val newReviewRef = ref.push()
                newReviewRef.setValue(reviewWithImageUrl)
                    .addOnSuccessListener { callback(true, "Book review created successfully") }
                    .addOnFailureListener { e -> callback(false, e.message ?: "Failed to create book review") }
            } else {
                callback(false, "Failed to upload book cover image to Cloudinary")
            }
        })
    }

    // Old signature for interface compatibility
    override fun createBookReview(review: BookReview, callback: (Boolean, String) -> Unit) {
        callback(false, "Use createBookReview(context, review, callback) instead.")
    }

    override fun getAllBookReviews(callback: (List<BookReview>?, String?) -> Unit) {
        ref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviews = mutableListOf<BookReview>()
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (child in snapshot.children) {
                        val review = child.getValue(BookReview::class.java)
                        if (review != null) {
                            reviews.add(review)
                        }
                    }
                }
                callback(reviews, null)
            } else {
                callback(null, task.exception?.message ?: "Failed to fetch book reviews")
            }
        }
    }

    override fun getBookReviewById(reviewId: String, callback: (BookReview?, String?) -> Unit) {
        ref.child(reviewId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    val review = snapshot.getValue(BookReview::class.java)
                    callback(review, null)
                } else {
                    callback(null, "Book review not found")
                }
            } else {
                callback(null, task.exception?.message ?: "Failed to fetch book review")
            }
        }
    }

    override fun updateBookReview(reviewId: String, review: BookReview, callback: (Boolean, String) -> Unit) {
        // If a new imageUri is provided, upload to Cloudinary first
        if (review.imageUri != null) {
            // Assume context is required, so this method should be called from an implementation that can provide it
            callback(false, "Context required for image upload. Use updateBookReviewWithImage(context, reviewId, review, callback)")
            return
        } else {
            // Remove imageUri before updating in Firebase
            val reviewToUpdate = review.copy(imageUri = null)
            ref.child(reviewId).setValue(reviewToUpdate)
                .addOnSuccessListener { callback(true, "Book review updated successfully") }
                .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update book review") }
        }
    }

    fun updateBookReviewWithImage(context: Context, reviewId: String, review: BookReview, callback: (Boolean, String) -> Unit) {
        if (review.imageUri != null) {
            uploadImageToCloudinary(context, review.imageUri) { success, imageUrl ->
                if (success && imageUrl != null) {
                    val reviewToUpdate = review.copy(imageUrl = imageUrl, imageUri = null)
                    ref.child(reviewId).setValue(reviewToUpdate)
                        .addOnSuccessListener { callback(true, "Book review updated successfully") }
                        .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update book review") }
                } else {
                    callback(false, "Failed to upload new book cover image to Cloudinary")
                }
            }
        } else {
            // No new image, just update other fields
            val reviewToUpdate = review.copy(imageUri = null)
            ref.child(reviewId).setValue(reviewToUpdate)
                .addOnSuccessListener { callback(true, "Book review updated successfully") }
                .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update book review") }
        }
    }

    override fun deleteBookReview(reviewId: String, callback: (Boolean, String) -> Unit) {
        ref.child(reviewId).removeValue()
            .addOnSuccessListener { callback(true, "Book review deleted successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to delete book review") }
    }

    override fun getBookReviewsByUser(username: String, callback: (List<BookReview>?, String?) -> Unit) {
        ref.orderByChild("username").equalTo(username).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviews = mutableListOf<BookReview>()
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (child in snapshot.children) {
                        val review = child.getValue(BookReview::class.java)
                        if (review != null) {
                            reviews.add(review)
                        }
                    }
                }
                callback(reviews, null)
            } else {
                callback(null, task.exception?.message ?: "Failed to fetch book reviews by user")
            }
        }
    }

    override fun getBookReviewsByGenre(genre: String, callback: (List<BookReview>?, String?) -> Unit) {
        ref.orderByChild("genre").equalTo(genre).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviews = mutableListOf<BookReview>()
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (child in snapshot.children) {
                        val review = child.getValue(BookReview::class.java)
                        if (review != null) {
                            reviews.add(review)
                        }
                    }
                }
                callback(reviews, null)
            } else {
                callback(null, task.exception?.message ?: "Failed to fetch book reviews by genre")
            }
        }
    }

    override fun getBookReviewsByAuthor(author: String, callback: (List<BookReview>?, String?) -> Unit) {
        ref.orderByChild("author").equalTo(author).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviews = mutableListOf<BookReview>()
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (child in snapshot.children) {
                        val review = child.getValue(BookReview::class.java)
                        if (review != null) {
                            reviews.add(review)
                        }
                    }
                }
                callback(reviews, null)
            } else {
                callback(null, task.exception?.message ?: "Failed to fetch book reviews by author")
            }
        }
    }

    fun uploadImageToCloudinary(
        context: Context,
        imageUri: Uri,
        onResult: (Boolean, String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                // ✅ Run UI updates on the Main Thread
                Handler(Looper.getMainLooper()).post {
                    onResult(true, imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    onResult(false,null)
                }
            }
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}