package com.kamkle.poker

import kotlinx.browser.document
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalWasmJsInterop::class)
actual fun initializeFirebase(apiKey: String, projectId: String, dbUrl: String) {
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

        set(dbRef, message.toJsString()).await<JsAny>()
    }

    override suspend fun getDataAt(path: String): List<DatabaseEntry> {
        val nodeRef = ref(db, "/")
        val snapshot: DataSnapshot = get(nodeRef).await()

        val result = mutableListOf<DatabaseEntry>()
        consoleLog(snapshot)
        if (snapshot.exists()) {
            snapshot.forEach {
                consoleLog(it.getValue()?.toJsString())
                result.add(DatabaseEntry(it.key ?: "", it.getValue().orEmpty()))
                false

            }
        }

        return result
    }

    override fun observePath(path: String): Flow<List<DatabaseEntry>> = callbackFlow {
        val pathRef = ref(db, path)
        val unsubscribe = onValue(
            query = pathRef,
            callback = { snapshot ->
                if (snapshot.exists()) {
                    val result = mutableListOf<DatabaseEntry>()

                    snapshot.forEach {
                        consoleLog(it.getValue()?.toJsString())
                        result.add(DatabaseEntry(it.key ?: "", it.getValue().orEmpty()))
                        false
                    }
                    trySend(result)

                } else {
                    print("empty")
                }
            },
            cancelCallback = { error ->
                close(RuntimeException("Firebase Error: ${error.message}"))
            }
        )

        // Ważne: onValue zwraca funkcję, którą musimy wywołać przy zamknięciu
        awaitClose {
            unsubscribe()
        }
    }

}