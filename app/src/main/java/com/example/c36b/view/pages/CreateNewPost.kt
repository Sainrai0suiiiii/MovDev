package com.example.c36b.view.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.c36b.repository.UserRepositoryImpl

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateBookReview() {
    var selectedGenre by remember { mutableStateOf("Fiction") }
    val genres = listOf("Fiction", "Non-Fiction", "Mystery", "Romance", "Sci-Fi", "Fantasy", "Biography", "Self-Help", "Thriller", "Historical")

    var bookTitle by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3.0f) }
    var review by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val userRepo = remember { UserRepositoryImpl() }
    val currentUser = userRepo.getCurrentUser()
    var username by remember { mutableStateOf("") }
    LaunchedEffect(currentUser) {
        currentUser?.let {
            userRepo.getUserFromDatabase(it.uid) { success, _, userModel ->
                if (success && userModel != null) {
                    username = userModel.name
                }
            }
        }
    }

    var isPublishing by remember { mutableStateOf(false) }

    LazyColumn (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp))
    {
        item{

        Text("Create Book Review", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Genre Chips
        Text("Genre", style = MaterialTheme.typography.labelMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            genres.forEach { genre ->
                FilterChip(
                    selected = selectedGenre == genre,
                    onClick = { selectedGenre = genre },
                    label = {
                        Text(genre)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2563EB),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFE5E7EB)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Book Title Field
        Text(text = "Book Title", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            placeholder = { Text("Enter book title...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Author Field
        Text(text = "Author", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            placeholder = { Text("Enter author name...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Field
        Text(text = "Rating: ${rating}/5.0", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < rating.toInt()) Color(0xFFFFD700) else Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 0f..5f,
            steps = 9,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Book Cover Image
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF2563EB)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2563EB))
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Book Cover", tint = Color(0xFF2563EB))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add book cover image")
            }
        }
        if (imageUri != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Review Content Field
        Text(text = "Your Review", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = review,
            onValueChange = { review = it },
            placeholder = { Text("Write your book review here...") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reviewer Name Field
        Text(text = "Reviewer Name", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {},
            placeholder = { Text("Enter your name...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tags Field
        Text(text = "Tags (comma separated)", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        var tags by remember { mutableStateOf("") }
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            placeholder = { Text("e.g. must-read, classic, page-turner") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                isPublishing = true
                val tagList = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val bookReview = com.example.c36b.model.BookReview(
                    username = username,
                    time = System.currentTimeMillis().toString(),
                    bookTitle = bookTitle,
                    author = author,
                    rating = rating,
                    review = review,
                    genre = selectedGenre,
                    tags = tagList,
                    likes = 0,
                    comments = 0,
                    shares = 0,
                    imageUri = imageUri
                )
                val postRepo = com.example.c36b.repository.PostRepositoryImpl()
                postRepo.createBookReview(context, bookReview) { success, message ->
                    isPublishing = false
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            enabled = !isPublishing
        ) {
            Text(if (isPublishing) "Publishing..." else "Publish Review", color = Color.White)
        }
    }
    }

}
