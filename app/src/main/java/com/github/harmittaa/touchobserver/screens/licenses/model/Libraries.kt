package com.github.harmittaa.touchobserver.screens.licenses.model

import kotlinx.serialization.Serializable

@Serializable
data class Libraries(
    val libraries: List<Library>
)

@Serializable
data class ArtifactId(
    val group: String,
    val name: String,
    val version: String
)

@Serializable
data class Library(
    val artifactId: ArtifactId,
    val libraryName: String,
    val license: String? = null,
    val licenseUrl: String? = null,
    val normalizedLicense: String,
    val url: String? = null
)
