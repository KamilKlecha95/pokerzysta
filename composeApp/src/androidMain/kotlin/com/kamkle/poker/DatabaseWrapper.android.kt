package com.kamkle.poker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

actual fun getPlatformDatabase(): MyDatabase = WasmDatabase()
actual fun initializeFirebase(apiKey: String, projectId: String, dbUrl: String){

}
class WasmDatabase : MyDatabase {


    override suspend fun writeMessage(path: String, message: String) {

    }

    override suspend fun getDataAt(path: String): List<DatabaseEntry> = listOf()
    override fun observePath(path: String): Flow<List<DatabaseEntry>> = flow{}


}
