package com.abahz.africa

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.abahz.africa.di.supabaseModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(supabaseModule)
    }
    ComposeViewport {
        App()
    }
}
