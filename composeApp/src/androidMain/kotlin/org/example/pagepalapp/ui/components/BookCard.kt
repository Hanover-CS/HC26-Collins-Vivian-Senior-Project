package org.example.pagepalapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.example.pagepalapp.R
import org.example.pagepalapp.data.Volume

@Composable
fun BookCard(
    volume: Volume,
    onClick: (Volume) -> Unit = {}
) {
    val info = volume.volumeInfo
    val title = info?.title ?: "Unknown title"
    val author = info?.authors?.joinToString(", ") ?: "Unknown author"
    val thumbnailUrl = info?.imageLinks?.thumbnail?.replace("http://", "https://")

    val hasProgress = volume.totalPages > 0 && volume.currentPage > 0
    val progress =
        if (hasProgress) (volume.currentPage.toFloat() / volume.totalPages.toFloat()).coerceIn(0f, 1f)
        else 0f

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(volume) }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // --- Thumbnail ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_book_placeholder),
                error = painterResource(R.drawable.ic_broken_image)
            )

            // --- Text info + progress ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2
                )
                Text(
                    text = "by $author",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (hasProgress) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}% read (${volume.currentPage}/${volume.totalPages})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
