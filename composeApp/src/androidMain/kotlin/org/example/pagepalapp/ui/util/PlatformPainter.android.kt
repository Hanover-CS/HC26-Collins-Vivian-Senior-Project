/**
 * android actual implementation for loading placeholder + error images.
 */
package org.example.pagepalapp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.example.pagepalapp.R

@Composable
actual fun platformPainter(name: String): Painter {
    return when (name) {
        "placeholder" -> painterResource(R.drawable.ic_book_placeholder)
        "error" -> painterResource(R.drawable.ic_broken_image)
        else -> painterResource(R.drawable.ic_book_placeholder)
    }
}