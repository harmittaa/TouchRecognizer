package com.github.harmittaa.touchobserver.repository

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun getDataStore(context: Context): DataStore<Preferences> = context.createDataStore(
    name = "consent", corruptionHandler = null, migrations = listOf(), scope = CoroutineScope(
        Dispatchers.IO + SupervisorJob())
)
