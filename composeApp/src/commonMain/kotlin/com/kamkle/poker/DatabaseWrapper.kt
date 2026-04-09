package com.kamkle.poker

interface MyDatabase {
    suspend fun writeMessage(path: String, message: String)
}

expect fun getPlatformDatabase(): MyDatabase
expect fun initializeFirebase(apiKey: String, projectId: String, dbUrl: String)