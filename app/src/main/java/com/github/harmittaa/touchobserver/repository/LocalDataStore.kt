package com.github.harmittaa.touchobserver.repository

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalDataStore(private val dataStore: DataStore<Preferences>) {
    private val hasConsent = preferencesKey<Boolean>("consent_given")

    suspend fun hasGivenConsent(): Boolean {
        return dataStore.data.map {
            it[hasConsent] ?: false
        }.first()
    }

    suspend fun storeConsent() {
        dataStore.edit {
            it[hasConsent] = true
        }
    }
}
