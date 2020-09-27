package com.github.harmittaa.touchobserver.repository

sealed class Resource {
    object Success : Resource()
    class Failure(val reason: String) : Resource()
}
