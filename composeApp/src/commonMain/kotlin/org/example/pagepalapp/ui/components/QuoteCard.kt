package org.example.pagepalapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.example.pagepalapp.ui.quotes.randomQuote

@Composable
fun QuoteCard() {

    // picks one quote at random per screen load
    val quote = remember { randomQuote() }

    // fade animation !
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1200),
        label = "quoteFade"
    )

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "✨ Daily Motivation",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = quote,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
