package com.github.harmittaa.touchobserver.screens.licenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.harmittaa.touchobserver.screens.licenses.model.Libraries
import java.io.InputStream
import java.nio.charset.Charset
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

class LicenseViewModel : ViewModel() {
    private val _libraries = MutableLiveData<Libraries>()
    val libraries: LiveData<Libraries> = _libraries

    fun generateLicensePojos(open: InputStream) {
        val size = open.available()
        val buffer = ByteArray(size)
        open.read(buffer)
        open.close()
        val json = String(buffer, Charset.forName("UTF-8"))
        Timber.d("DEBUGG: $json")

        _libraries.value = Json.decodeFromString<Libraries>(json)
    }
}
