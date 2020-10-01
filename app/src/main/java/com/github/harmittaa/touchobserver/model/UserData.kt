package com.github.harmittaa.touchobserver.model

data class UserData(val gender: Gender, val handedness: Handedness) {
    enum class Gender { MALE, FEMALE }
    enum class Handedness { LEFT, RIGHT }
}
