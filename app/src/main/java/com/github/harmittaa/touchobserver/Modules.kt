package com.github.harmittaa.touchobserver

import org.koin.dsl.module

val viewModelModule = module {
    factory { SwipeViewModel() }
}