package com.github.harmittaa.touchobserver.model

data class DataRemoval(
    val requested: Boolean = true,
    val timeStamp: Long = System.currentTimeMillis()
)
