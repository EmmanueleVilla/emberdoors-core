package com.shadowings.emberdoors.terminal

import com.shadowings.emberdoors.logic.CombatEngine
import com.shadowings.emberdoors.logic.DungeonGenerator
import com.shadowings.emberdoors.logic.GameSession
import com.shadowings.emberdoors.model.Element
import com.shadowings.emberdoors.model.combat.CombatAction
import com.shadowings.emberdoors.model.door.DoorContent
import com.shadowings.emberdoors.model.enemy.Enemy

fun main() {
    println("""
        ====================================================
           🔥  E M B E R D O O R S  -  T E R M I N A L  🔥
        ====================================================
        Benvenuto nel dungeon delle Emberdoors!
        Scegli quale porta aprire, combatti i mostri e raccogli l'oro.
    """.trimIndent())

    var session = GameSession()

    while (!session.isGameOver) {
        printStatus(session)
        println("\nDavanti a te ci sono 3 porte sigillate:")
        session.currentDoors.forEach { door ->
            println(" [${door.id}] ${door.title}")
        }
        print("\nScegli una porta (1-3) o 'q' per uscire: ")
        val input = readlnOrNull()?.trim() ?: "q"

        if (input.lowercase() == "q") {
            println("Hai abbandonato l'avventura. Arrivederci!")
            break
        }

        val chosenDoorIndex = input.toIntOrNull()?.minus(1)
        if (chosenDoorIndex == null || chosenDoorIndex !in session.currentDoors.indices) {
            println(">> Scelta non valida, riprova!")
            continue
        }

        val chosenDoor = session.currentDoors[chosenDoorIndex]
        println("\n>>> Varchi la porta: ${chosenDoor.title} <<<")

        when (val content = chosenDoor.content) {
            is DoorContent.TreasureChest -> {
                println("🎉 Hai trovato un tesoro: ${content.description}")
                println("Ricevi +${content.gold} Monete d'Oro!")
                session = session.copy(
                    gold = session.gold + content.gold,
                    floor = session.floor + 1,
                    currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                )
            }
            is DoorContent.HealingFountain -> {
                val maxHp = session.hero.stats.survival.maxHp
                val newHp = (session.hero.currentHp + content.healAmount).coerceAtMost(maxHp)
                val healed = newHp - session.hero.currentHp
                println("💧 Ti abbeveri alla Fontana Ristoratrice: recuperi $healed HP!")
                session = session.copy(
                    hero = session.hero.copy(currentHp = newHp),
                    floor = session.floor + 1,
                    currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                )
            }
            is DoorContent.MonsterEncounter -> {
                val combatResult = runCombatLoop(session, content.enemy)
                if (combatResult == null) {
                    println("Sei fuggito dal gioco.")
                    break
                }
                session = combatResult
            }
        }
    }

    if (session.isGameOver) {
        println("\n====================================================")
        println("💀 GAME OVER! Sei sopravvissuto fino al Piano ${session.floor} accumulando ${session.gold} Oro.")
        println("====================================================")
    }
}

private fun printStatus(session: GameSession) {
    println("\n----------------------------------------------------")
    println("PIANO: ${session.floor}  |  ORO: ${session.gold}  |  HP: ${session.hero.currentHp}/${session.hero.stats.survival.maxHp}  |  ENERGIA: ${session.hero.currentEnergy}/${session.hero.stats.pace.maxEnergy}")
    println("----------------------------------------------------")
}

private fun runCombatLoop(session: GameSession, initialEnemy: Enemy): GameSession? {
    var heroState = session.hero
    var enemy = initialEnemy

    println("\n⚔️  INGAGGIO INIZIATO: ${enemy.name} (HP: ${enemy.currentHp}/${enemy.maxHp}, Danno: ${enemy.attackDamage})!")

    while (heroState.isAlive && enemy.isAlive) {
        println("\n-- STATO: Eroe HP: ${heroState.currentHp}/${heroState.stats.survival.maxHp} (Energia: ${heroState.currentEnergy}/${heroState.stats.pace.maxEnergy}) | Nemico HP: ${enemy.currentHp}/${enemy.maxHp} --")
        println("Azioni disponibili:")
        println(" 1) Attacco Base [0 Energia, ${heroState.stats.damage.attack} Danno]")
        println(" 2) Difesa (+${heroState.stats.survival.defenseReductionPercent}% Riduzione danno nemico)")
        println(" 3) Special Skill [2 Energia, ${heroState.stats.damage.special} Danno Fuoco]")
        println(" 4) Magia Arcana [3 Energia, ${heroState.stats.damage.magic} Danno]")
        println(" 5) Ultimate [5 Energia, ${heroState.stats.damage.ultimate} Danno]")
        print("Cosa vuoi fare? ")

        val choice = readlnOrNull()?.trim() ?: "1"
        val action = when (choice) {
            "2" -> CombatAction.Defend
            "3" -> CombatAction.SpecialSkill(element = Element.Fire)
            "4" -> CombatAction.MagicSpell()
            "5" -> CombatAction.Ultimate
            else -> CombatAction.BasicAttack
        }

        val turnResult = CombatEngine.executeTurn(heroState, enemy, action)
        heroState = turnResult.heroState
        enemy = turnResult.enemy

        turnResult.combatLog.forEach { log -> println("  > $log") }

        if (turnResult.isCombatOver) {
            if (turnResult.heroWon) {
                println("\n✨ VITTORIA! Hai sconfitto ${enemy.name}! Ricevi ${enemy.xpReward} Oro.")
                return session.copy(
                    hero = heroState,
                    gold = session.gold + enemy.xpReward,
                    floor = session.floor + 1,
                    currentDoors = DungeonGenerator.generateDoors(session.floor + 1)
                )
            } else {
                return session.copy(hero = heroState, isGameOver = true)
            }
        }
    }

    return session.copy(hero = heroState, isGameOver = !heroState.isAlive)
}
