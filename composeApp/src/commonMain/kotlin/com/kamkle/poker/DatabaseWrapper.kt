package com.kamkle.poker

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
data class DatabaseEntry(
    val id: String,
    val content: String
)

interface MyDatabase {
    suspend fun writeMessage(path: String, message: String)
    suspend fun getDataAt(path: String): List<DatabaseEntry>

    fun observePath(path: String): Flow<List<DatabaseEntry>>
}


expect fun getPlatformDatabase(): MyDatabase
expect fun initializeFirebase(apiKey: String, projectId: String, dbUrl: String)