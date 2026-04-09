@file:JsModule("firebase/database")
package com.kamkle.poker

import kotlin.js.Promise

// Moduł Realtime Database
external fun getDatabase(app: JsAny = definedExternally): JsAny
external fun ref(db: JsAny, path: String): JsAny
external fun set(reference: JsAny, value: JsAny): Promise<JsAny>
