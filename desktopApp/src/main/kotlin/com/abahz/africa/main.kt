package com.abahz.africa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.abahz.africa.di.supabaseModule
import com.abahz.africa.ui.AdminScreen
import com.abahz.africa.ui.ShopRegistrationScreen
import com.abahz.africa.ui.ShopSignInScreen
import org.koin.core.context.startKoin

enum class Screen {
    SignIn, Register, Admin
}

fun main() {
    startKoin {
        modules(supabaseModule)
    }
    application {
        var currentScreen by remember { mutableStateOf(Screen.SignIn) }

        Window(
            onCloseRequest = ::exitApplication,
            title = "Mama Africa - ${currentScreen.name}",
        ) {
            when (currentScreen) {
                Screen.SignIn -> ShopSignInScreen(
                    onNavigateToRegister = { currentScreen = Screen.Register },
                    onNavigateToAdmin = { currentScreen = Screen.Admin }
                )
                Screen.Register -> ShopRegistrationScreen(
                    onNavigateToLogin = { currentScreen = Screen.SignIn }
                )
                Screen.Admin -> AdminScreen()
            }
        }
    }
}
