@file:JsModule("firebase/database")
package com.kamkle.poker

import kotlin.js.Promise

// Moduł Realtime Database
external fun getDatabase(app: JsAny = definedExternally): JsAny
external fun ref(db: JsAny, path: String): JsAny
external fun set(reference: JsAny, value: JsAny): Promise<JsAny>

external fun get(reference: JsAny): Promise<DataSnapshot>

// Interfejs dla wyniku z Firebase
external interface DataSnapshot : JsAny {
    val key: String?

    @JsName("val")
    fun getValue(): String?

    fun exists(): Boolean
    fun forEach(action: (child: DataSnapshot) -> Boolean): Boolean
}

@JsFun("(snapshot) => snapshot.key")
external fun getSnapshotKey(snapshot: DataSnapshot): String

@JsFun("(obj) => String(obj)")
external fun jsAnyToString(obj: JsAny): String

@JsFun("(obj) => console.log(obj)")
external fun consoleLog(obj: JsAny?)
external interface DatabaseReference
external interface JsError {
    val message: String
    val stack: String?
}
external interface Unsubscribe {
    @JsName("invoke")
    operator fun invoke()
}
external fun onValue(
    query: JsAny,
    callback: (snapshot: DataSnapshot) -> Unit,
    cancelCallback: ((error: JsError) -> Unit)? = definedExternally
): Unsubscribe