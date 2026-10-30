package com.shadowings.emberdoors.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.concurrent.NotificationContext
import org.reduxkotlin.concurrent.createConcurrentStore
import org.reduxkotlin.thunk.createThunkMiddleware

data class AppDependencies(
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
)

fun createAppStore(
    dependencies: AppDependencies = AppDependencies(),
    notificationContext: NotificationContext = NotificationContext { it.run() },
    initialState: AppState = AppState(),
    enableLogging: Boolean = false,
): Store<AppState> {
    val middlewares = listOfNotNull(
        createThunkMiddleware<AppState>(dependencies),
        if (enableLogging) loggerMiddleware() else null,
    )
    return createConcurrentStore(
        reducer = rootReducer,
        preloadedState = initialState,
        notificationContext = notificationContext,
        enhancer = applyMiddleware(*middlewares.toTypedArray()),
    )
}
