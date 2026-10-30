package com.shadowings.emberdoors.logic

import com.shadowings.emberdoors.model.door.Door
import com.shadowings.emberdoors.model.hero.HeroStats

data class GameSession(
    val floor: Int = 1,
    val gold: Int = 0,
    val hero: HeroCombatState = HeroCombatState(
        currentHp = HeroStats().survival.maxHp,
        currentEnergy = HeroStats().pace.maxEnergy,
        stats = HeroStats()
    ),
    val currentDoors: List<Door> = DungeonGenerator.generateDoors(floor = 1),
    val isGameOver: Boolean = false,
)
