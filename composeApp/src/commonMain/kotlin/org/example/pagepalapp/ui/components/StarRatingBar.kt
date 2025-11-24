package org.example.pagepalapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StarRatingBar(
    rating: Double,
    onRatingChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val fullStars = rating.toInt()
    val hasHalfStar = (rating - fullStars) >= 0.5

    Row(modifier = modifier) {

        for (i in 1..5) {
            val symbol = when {
                i <= fullStars -> "★"       // full
                i == fullStars + 1 && hasHalfStar -> "*"   // half star
                else -> "☆"                // empty
            }

            Text(
                text = symbol,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clickable {
                        val newRating = when {
                            i == fullStars + 1 && !hasHalfStar ->
                                fullStars + 0.5         // click to give half star
                            else -> i.toDouble()         // full star
                        }
                        onRatingChange(newRating)
                    }
            )
        }
    }
}
