package org.example.pagepalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.example.pagepalapp.ui.App
import com.example.compose.AppTheme    // ← use your uploaded theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(dynamicColor = false) {   // disable Material You if you want your colors
                App()
            }
        }
    }
}
