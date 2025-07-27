package com.example.c36b.view.pages

import  androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.c36b.R
import com.example.c36b.view.components.BookReviewCard
import com.example.c36b.view.components.SearchBar
import androidx.compose.foundation.lazy.items
import com.example.c36b.model.BookReview
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToBookmarks: () -> Unit = {},
    onAddToBookmarks: (Int) -> Unit = {},
    onAddToFavorites: (Int) -> Unit = {},
    bookmarkCount: Int = 0,
    favoritesCount: Int = 0,
    modifier: Modifier = Modifier
) {

    // Fetch book reviews from Firebase
    val postViewModel: com.example.c36b.viewmodel.PostViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return com.example.c36b.viewmodel.PostViewModel(com.example.c36b.repository.PostRepositoryImpl()) as T
        }
    })
    var bookReviews by remember { mutableStateOf<List<com.example.c36b.model.BookReview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        postViewModel.getAllBookReviews { result, err ->
            if (result != null) {
                bookReviews = result
                isLoading = false
            } else {
                error = err
                isLoading = false
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "BookReviews",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Discover amazing books and reviews",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        SearchBar()

        Spacer(modifier = Modifier.height(12.dp))

        FilterChips()

        Spacer(modifier = Modifier.height(12.dp))
        when {
            isLoading -> {
                androidx.compose.material3.Text("Loading book reviews...")
            }
            error != null -> {
                androidx.compose.material3.Text("Error: $error")
            }
            else -> {
                LazyColumn {
                    items(bookReviews) { review ->
                        BookReviewCard(review)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChips() {
    val genres = listOf("All", "Fiction", "Non-Fiction", "Mystery", "Romance", "Sci-Fi", "Fantasy", "Biography", "Self-Help")
    var selected by remember { mutableStateOf("All") }

    LazyRow {
        items(genres.size) { index ->
            val genre = genres[index]
            FilterChip(
                selected = selected == genre,
                onClick = { selected = genre },
                label = { Text(genre) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
