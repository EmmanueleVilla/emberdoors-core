package com.shadowings.emberdoors.store

import org.reduxkotlin.Middleware

fun <State> loggerMiddleware(): Middleware<State> = { store ->
    { next ->
        { action ->
            println("[REDUX ACTION] $action")
            val result = next(action)
            println("[REDUX STATE]  ${store.state}")
            result
        }
    }
}
