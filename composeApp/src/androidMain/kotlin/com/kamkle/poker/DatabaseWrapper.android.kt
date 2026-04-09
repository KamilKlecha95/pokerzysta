package com.kamkle.poker

actual fun getPlatformDatabase(): MyDatabase = WasmDatabase()

class WasmDatabase : MyDatabase {

    override suspend fun writeString(path: String, value: String) {

    }
}
