/**
 * displays a single book entry inside lists such as:
 *  - search results on the Home screen
 *  - user’s saved library list
 *
 * shows:
 *  - book image (as a thumbnail)
 *  - title
 *  - author(s)
 */

package org.example.pagepalapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.example.pagepalapp.data.Volume
import org.example.pagepalapp.ui.util.platformPainter

@Composable
fun BookCard(
    volume: Volume,
    onClick: (Volume) -> Unit = {}
) {
    val info = volume.volumeInfo
    val title = info?.title ?: "Unknown title"        // fallback title
    val author = info?.authors?.joinToString(", ") ?: "Unknown author"
    val thumbnailUrl = info?.imageLinks?.thumbnail?.replace("http://", "https://")

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(volume) }            // triggers navigation or action
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFF3F0FF),       // soft gradient background
                            Color.White
                        )
                    )
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // book thumbnail image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = platformPainter("placeholder"),  // local fallback image
                error = platformPainter("error")
            )

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                // title text
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    )
                )
                Spacer(Modifier.height(4.dp))

                // author text
                Text(
                    author,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF5A5A5A)
                    )
                )
            }
        }
    }
}
