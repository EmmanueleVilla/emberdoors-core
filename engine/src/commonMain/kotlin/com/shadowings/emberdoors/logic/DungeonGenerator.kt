package com.shadowings.emberdoors.logic

import com.shadowings.emberdoors.model.Element
import com.shadowings.emberdoors.model.door.Door
import com.shadowings.emberdoors.model.door.DoorContent
import com.shadowings.emberdoors.model.enemy.Enemy
import kotlin.random.Random

object DungeonGenerator {

    private val monsterTemplates = listOf(
        Enemy("imp", "Imp Infernale", maxHp = 18, attackDamage = 4, speed = 4, element = Element.Fire),
        Enemy("snake", "Vipera Velenosa", maxHp = 22, attackDamage = 5, speed = 6, element = Element.Poison),
        Enemy("skeleton", "Guerriero Scheletro", maxHp = 30, attackDamage = 7, speed = 3),
        Enemy("orc", "Orco Furioso", maxHp = 42, attackDamage = 9, speed = 2),
        Enemy("demon", "Demone Minore", maxHp = 35, attackDamage = 8, speed = 5, element = Element.Fire)
    )

    fun generateDoors(floor: Int, random: Random = Random): List<Door> {
        val doors = mutableListOf<Door>()
        for (i in 1..3) {
            val roll = random.nextInt(100)
            val content = when {
                roll < 65 -> {
                    val base = monsterTemplates.random(random)
                    val scaledHp = base.maxHp + (floor * 3)
                    val scaledDmg = base.attackDamage + (floor / 2)
                    DoorContent.MonsterEncounter(
                        base.copy(
                            maxHp = scaledHp,
                            currentHp = scaledHp,
                            attackDamage = scaledDmg
                        )
                    )
                }
                roll < 85 -> {
                    DoorContent.TreasureChest(gold = 15 + floor * 5, description = "Uno scrigno antico ricco di monete d'oro.")
                }
                else -> {
                    DoorContent.HealingFountain(healAmount = 15 + floor * 2)
                }
            }

            val title = when (content) {
                is DoorContent.MonsterEncounter -> "Porta Oscura (#$i) - Presenza Ostile: ${content.enemy.name}"
                is DoorContent.TreasureChest -> "Porta Dorata (#$i) - Scintillio misterioso"
                is DoorContent.HealingFountain -> "Porta Luminosa (#$i) - Aura ristoratrice"
            }

            doors.add(
                Door(
                    id = i,
                    title = title,
                    description = "Una porta intrisa di energia elementale.",
                    content = content
                )
            )
        }
        return doors
    }
}
