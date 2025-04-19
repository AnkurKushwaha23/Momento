package com.ankurkushwaha.momento.domain.model

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:39
 */
data class Notes(
    val id: Int = 0,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)
