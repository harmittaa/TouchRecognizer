package com.github.harmittaa.touchobserver.di

import com.github.harmittaa.touchobserver.SwipeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { SwipeViewModel() }
}