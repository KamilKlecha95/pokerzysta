package com.kamkle.poker
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    override suspend fun getDataAt(path: String): List<DatabaseEntry> {
        val nodeRef = ref(db, "/")
        val snapshot: DataSnapshot = get(nodeRef).await()

        val result = mutableListOf<DatabaseEntry>()
        consoleLog(snapshot)
        if (snapshot.exists()) {
            // forEach w RTDB iteruje po kluczach Twojego obiektu
            snapshot.forEach {
                consoleLog(it.`val`())
                val v = jsToString(it.`val`())
            result.add(DatabaseEntry(it.key?: "", v))
                false

            }
        }
        print("result $result")

        return result
    }

   override fun observePath(path: String): Flow<List<DatabaseEntry>> = callbackFlow {
        val pathRef = ref(db, path)
        val result = mutableListOf<DatabaseEntry>()

        val unsubscribe = onValue(
            query = pathRef,
            callback = { snapshot ->
                if (snapshot.exists()) {
                    snapshot.forEach {
                        consoleLog(it.`val`())
                        val v = com.kamkle.poker.jsToString(it.`val`())
                        result.add(DatabaseEntry(it.key ?: "", v))
                        false
                    }
                    trySend(result)


                } else {
                    print("empty")
                }
            },
            cancelCallback = { error ->
                // Tutaj bezpiecznie dobieramy się do message z JsError
                close(RuntimeException("Firebase Error: ${error.message}"))
            }
        )

        // Ważne: onValue zwraca funkcję, którą musimy wywołać przy zamknięciu
        awaitClose {
            unsubscribe()
        }
    }

}

@JsFun("(obj) => String(obj)")
external fun jsToString(obj: JsAny?): String