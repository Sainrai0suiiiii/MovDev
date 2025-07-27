package com.example.c36b.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c36b.model.BookReview
import coil.compose.rememberAsyncImagePainter

@Composable
fun BookReviewCard(review: BookReview) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            // Book cover image
            Image(
                painter = rememberAsyncImagePainter(review.imageUrl),
                contentDescription = "Book cover: ${review.bookTitle}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Book title
            Text(
                text = review.bookTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Author
            Text(
                text = "by ${review.author}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < review.rating.toInt()) Color(0xFFFFD700) else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${review.rating}/5.0",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Genre
            AssistChip(
                onClick = {},
                label = { Text(review.genre) },
                modifier = Modifier.padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Review content
            Text(
                text = review.review,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tags
            if (review.tags.isNotEmpty()) {
                Row {
                    review.tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text("#$tag") },
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // User info and engagement
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(review.username, fontSize = 12.sp, color = Color.Gray)
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconWithText(Icons.Default.Favorite, review.likes)
                    Spacer(modifier = Modifier.width(12.dp))
                    IconWithText(Icons.Default.Email, review.comments)
                    Spacer(modifier = Modifier.width(12.dp))
                    IconWithText(Icons.Default.Share, review.shares)
                }
            }
        }
    }
}

@Composable
fun IconWithText(icon: ImageVector, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("$count", fontSize = 12.sp)
    }
}
