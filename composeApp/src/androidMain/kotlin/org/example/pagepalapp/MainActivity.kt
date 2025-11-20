/**
 * standard Android entry point for the app
 */

package org.example.pagepalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.example.pagepalapp.ui.App

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // launch the Compose UI
        setContent {
            App()
        }
    }
}
