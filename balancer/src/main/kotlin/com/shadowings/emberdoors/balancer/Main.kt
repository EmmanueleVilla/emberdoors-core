package com.shadowings.emberdoors.balancer

import com.shadowings.emberdoors.logic.CombatEngine
import com.shadowings.emberdoors.logic.DungeonGenerator
import com.shadowings.emberdoors.logic.GameSession
import com.shadowings.emberdoors.logic.HeroCombatState
import com.shadowings.emberdoors.model.Element
import com.shadowings.emberdoors.model.combat.CombatAction
import com.shadowings.emberdoors.model.door.Door
import com.shadowings.emberdoors.model.door.DoorContent
import com.shadowings.emberdoors.model.enemy.Enemy
import kotlin.random.Random

/**
 * Agente IA che prende decisioni strategiche di gioco.
 */
class BalancerAiAgent(private val random: Random = Random(42)) {

    fun chooseDoor(session: GameSession, doors: List<Door>): Door {
        val hpRatio = session.hero.currentHp.toDouble() / session.hero.stats.survival.maxHp
        // Se la vita è sotto il 40%, cerca prioritariamente la fontana di cura
        if (hpRatio < 0.40) {
            val fountain = doors.firstOrNull { it.content is DoorContent.HealingFountain }
            if (fountain != null) return fountain
        }
        // Altrimenti cerca tesori prima dei mostri
        val treasure = doors.firstOrNull { it.content is DoorContent.TreasureChest }
        if (treasure != null && random.nextBoolean()) return treasure

        // Se ci sono mostri, scegli il mostro con meno HP per minimizzare il rischio
        val monsters = doors.filter { it.content is DoorContent.MonsterEncounter }
        if (monsters.isNotEmpty()) {
            return monsters.minByOrNull { (it.content as DoorContent.MonsterEncounter).enemy.maxHp } ?: doors.first()
        }

        return doors.random(random)
    }

    fun chooseCombatAction(hero: HeroCombatState, enemy: Enemy): CombatAction {
        // 1. Se possiamo abbattere il nemico con un attacco base, risparmiamo energia
        if (enemy.currentHp <= hero.stats.damage.attack) {
            return CombatAction.BasicAttack
        }

        // 2. Se possiamo abbatterlo con la Special e abbiamo 2 energia
        val specialDmg = hero.stats.damage.special
        if (hero.currentEnergy >= 2 && enemy.currentHp <= specialDmg) {
            return CombatAction.SpecialSkill(element = Element.Fire)
        }

        // 3. Se abbiamo energia piena (5), scateniamo l'Ultimate!
        if (hero.currentEnergy >= 5) {
            return CombatAction.Ultimate
        }

        // 4. Se l'eroe rischia di morire nel prossimo turno e non può uccidere il nemico, difendi
        val estimatedEnemyDmg = (enemy.attackDamage - hero.stats.survival.armor).coerceAtLeast(1)
        if (hero.currentHp <= estimatedEnemyDmg && !hero.isDefending) {
            return CombatAction.Defend
        }

        // 5. Se abbiamo 2 o più energia, usa l'abilità speciale
        if (hero.currentEnergy >= 2) {
            return CombatAction.SpecialSkill(element = Element.Fire)
        }

        // 6. Default: attacco base
        return CombatAction.BasicAttack
    }
}

data class SimulationReport(
    val totalRuns: Int,
    val averageFloorsReached: Double,
    val maxFloorReached: Int,
    val averageGoldCollected: Double,
    val deathsByMonster: Map<String, Int>,
    val averageCombatTurns: Double,
)

object BalanceSimulator {

    fun runSimulation(runs: Int = 1000, maxFloorsCap: Int = 50): SimulationReport {
        val ai = BalancerAiAgent()
        var totalFloors = 0
        var maxFloor = 0
        var totalGold = 0
        var totalTurnsCount = 0
        var totalCombats = 0
        val deathsByMonster = mutableMapOf<String, Int>()

        for (i in 1..runs) {
            var session = GameSession()

            while (!session.isGameOver && session.floor < maxFloorsCap) {
                val chosenDoor = ai.chooseDoor(session, session.currentDoors)
                when (val content = chosenDoor.content) {
                    is DoorContent.TreasureChest -> {
                        session = session.copy(
                            gold = session.gold + content.gold,
                            floor = session.floor + 1,
                            currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                        )
                    }
                    is DoorContent.HealingFountain -> {
                        val maxHp = session.hero.stats.survival.maxHp
                        val newHp = (session.hero.currentHp + content.healAmount).coerceAtMost(maxHp)
                        session = session.copy(
                            hero = session.hero.copy(currentHp = newHp),
                            floor = session.floor + 1,
                            currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                        )
                    }
                    is DoorContent.MonsterEncounter -> {
                        var heroState = session.hero
                        var enemy = content.enemy
                        var turnsInCombat = 0

                        while (heroState.isAlive && enemy.isAlive) {
                            turnsInCombat++
                            val action = ai.chooseCombatAction(heroState, enemy)
                            val result = CombatEngine.executeTurn(heroState, enemy, action)
                            heroState = result.heroState
                            enemy = result.enemy
                        }

                        totalTurnsCount += turnsInCombat
                        totalCombats++

                        if (heroState.isAlive) {
                            session = session.copy(
                                hero = heroState,
                                gold = session.gold + enemy.xpReward,
                                floor = session.floor + 1,
                                currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                            )
                        } else {
                            deathsByMonster[enemy.name] = (deathsByMonster[enemy.name] ?: 0) + 1
                            session = session.copy(hero = heroState, isGameOver = true)
                        }
                    }
                }
            }

            totalFloors += session.floor
            if (session.floor > maxFloor) maxFloor = session.floor
            totalGold += session.gold
        }

        return SimulationReport(
            totalRuns = runs,
            averageFloorsReached = totalFloors.toDouble() / runs,
            maxFloorReached = maxFloor,
            averageGoldCollected = totalGold.toDouble() / runs,
            deathsByMonster = deathsByMonster,
            averageCombatTurns = if (totalCombats > 0) totalTurnsCount.toDouble() / totalCombats else 0.0,
        )
    }
}

fun main() {
    println("""
        ====================================================
           🤖  E M B E R D O O R S  -  B A L A N C E R  🤖
        ====================================================
        Avvio simulazione IA di bilanciamento delle partite...
    """.trimIndent())

    val runs = 1000
    val report = BalanceSimulator.runSimulation(runs = runs)

    println("\nRISULTATI SIMULAZIONE ($runs partite):")
    println("----------------------------------------------------")
    println("Piano Medio Raggiunto:    ${"%.2f".format(report.averageFloorsReached)}")
    println("Piano Massimo Raggiunto:  ${report.maxFloorReached}")
    println("Oro Medio Guadagnato:     ${"%.2f".format(report.averageGoldCollected)}")
    println("Durata Media Combattimento: ${"%.2f".format(report.averageCombatTurns)} turni")
    println("\nStatistiche Letalità Mostri (Cause di Morte dell'Eroe):")
    report.deathsByMonster.entries
        .sortedByDescending { it.value }
        .forEach { (monster, count) ->
            val percentage = (count.toDouble() / report.totalRuns) * 100
            println(" - %-25s : %4d morti (%5.1f%% delle partite)".format(monster, count, percentage))
        }
    println("----------------------------------------------------")
    println("Simulazione completata con successo!")
}
