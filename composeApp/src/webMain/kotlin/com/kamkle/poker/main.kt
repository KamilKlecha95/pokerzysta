package com.kamkle.poker

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        initializeFirebase(
            apiKey = "AIzaSyTwojPrawdziwyKlucz...",
            projectId = "twoj-id-projektu",
            dbUrl = "https://twoj-projekt-default-rtdb.europe-west1.firebasedatabase.app"
        )
        App()
    }
}