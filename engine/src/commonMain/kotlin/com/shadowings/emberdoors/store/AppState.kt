package com.shadowings.emberdoors.store

import com.shadowings.emberdoors.logic.GameSession

data class AppState(
    val gameSession: GameSession = GameSession(),
)
