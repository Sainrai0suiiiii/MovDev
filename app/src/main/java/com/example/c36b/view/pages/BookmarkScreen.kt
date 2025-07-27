package com.example.c36b.view.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.c36b.model.BookReview
import com.example.c36b.view.components.BookReviewCard

@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier
) {
    // For now, we'll show a placeholder since we don't have actual bookmarked reviews
    // In a real app, you would fetch bookmarked reviews from a database
    val bookmarkedReviews: List<BookReview> = emptyList()
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bookmarked Reviews",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Text(
                text = "0 bookmarks",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Text(
                text = "No bookmarked reviews yet.\nStart exploring and bookmark your favorite book reviews!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}