package com.kamkle.poker
import kotlinx.coroutines.await

// 1. Implementacja inicjalizacji Firebase
@OptIn(ExperimentalWasmJsInterop::class)
actual fun initializeFirebase(apiKey: String, projectId: String, dbUrl: String) {
    // Wywołujemy naszą bezpieczną dla Wasm funkcję pomocniczą
    val options = createFirebaseOptions(apiKey, projectId, dbUrl)
    initializeApp(options)
}

private fun createFirebaseOptions(apiKey: String, projectId: String, dbUrl: String): JsAny =
    js("({ apiKey: apiKey, projectId: projectId, databaseURL: dbUrl })")

actual fun getPlatformDatabase(): MyDatabase = WebFirebaseDatabase()

@OptIn(ExperimentalWasmJsInterop::class)
class WebFirebaseDatabase : MyDatabase {

    private val db = getDatabase()

    override suspend fun writeMessage(path: String, message: String) {
        val dbRef = ref(db, path)

        val result : JsAny = set(dbRef, message.toJsString()).await()
    }
}