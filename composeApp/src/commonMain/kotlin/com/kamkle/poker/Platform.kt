package com.kamkle.poker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform