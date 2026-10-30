package com.shadowings.emberdoors.store

import org.reduxkotlin.Reducer

sealed interface GameAction {
    data class UpdateSession(val session: com.shadowings.emberdoors.logic.GameSession) : GameAction
    data object ResetGame : GameAction
}

val rootReducer: Reducer<AppState> = { state, action ->
    when (action) {
        is GameAction.UpdateSession -> state.copy(gameSession = action.session)
        is GameAction.ResetGame -> state.copy(gameSession = com.shadowings.emberdoors.logic.GameSession())
        else -> state
    }
}
