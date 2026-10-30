package com.shadowings.emberdoors.model.enemy

import com.shadowings.emberdoors.model.Element

data class Enemy(
    val id: String,
    val name: String,
    val maxHp: Int,
    val currentHp: Int = maxHp,
    val attackDamage: Int,
    val speed: Int,
    val element: Element? = null,
    val xpReward: Int = 10,
) {
    val isAlive: Boolean get() = currentHp > 0
}
