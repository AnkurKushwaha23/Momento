package com.ankurkushwaha.momento.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:18
 */
@Serializable
sealed class Screens {
    @Serializable
    data object NotesScreen : Screens()

    @Serializable
    data object TaskScreen : Screens()
}