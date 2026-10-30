package com.shadowings.emberdoors.model.door

import com.shadowings.emberdoors.model.enemy.Enemy

sealed interface DoorContent {
    data class MonsterEncounter(val enemy: Enemy) : DoorContent
    data class TreasureChest(val gold: Int, val description: String) : DoorContent
    data class HealingFountain(val healAmount: Int) : DoorContent
}

data class Door(
    val id: Int,
    val title: String,
    val description: String,
    val content: DoorContent,
)
