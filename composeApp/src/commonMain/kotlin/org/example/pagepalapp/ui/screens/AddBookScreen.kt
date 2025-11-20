/**
 * allows the user to manually add their own personal books
 * The user enters:
 *  - Title
 *  - Author
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.data.Volume
import org.example.pagepalapp.data.VolumeInfo
import org.example.pagepalapp.data.ImageLinks

@Composable
fun AddBookScreen(navController: NavController, viewModel: HomeViewModel) {

    var title by remember { mutableStateOf("") }     // controlled input
    var author by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add a Personal Book") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")                     // simple back arrow icon
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // title input field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // author input field
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // save button
            Button(
                onClick = {
                    // manually create a new local book object
                    val newBook = Volume(
                        id = "user_${System.currentTimeMillis()}",    // unique ID
                        volumeInfo = VolumeInfo(
                            title = title,
                            authors = listOf(author),
                            description = "Personal book",
                            imageLinks = ImageLinks(thumbnail = null)
                        ),
                        currentPage = 0,
                        totalPages = 0
                    )

                    viewModel.addToLibrary(newBook)   // add to saved library
                    navController.popBackStack()      // return to previous screen
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
