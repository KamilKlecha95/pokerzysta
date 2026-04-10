package com.kamkle.poker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        initializeFirebase(
            apiKey = "AIzaSyB5TueU3MPZtX1MIF7jT7xpQCwG5zXb3Eg...",
            projectId = "pokerzysta-c3416",
            dbUrl = "https://pokerzysta-c3416-default-rtdb.firebaseio.com"
        )
        App()
    }
}