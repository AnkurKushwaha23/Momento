package com.ankurkushwaha.momento.presentation.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 19:25
 */

class BottomNavViewModel : ViewModel() {
    private val _currentScreen = MutableStateFlow<Screens>(Screens.NotesScreen)
    val currentScreen = _currentScreen.asStateFlow()

    fun updateScreen(screens: Screens){
        _currentScreen.value = screens
    }
}