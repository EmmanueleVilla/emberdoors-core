package com.shadowings.emberdoors.logic

import com.shadowings.emberdoors.model.combat.CombatAction
import com.shadowings.emberdoors.model.enemy.Enemy
import com.shadowings.emberdoors.model.hero.HeroStats
import kotlin.math.max

data class HeroCombatState(
    val currentHp: Int,
    val currentEnergy: Int,
    val stats: HeroStats = HeroStats(),
    val isDefending: Boolean = false,
) {
    val isAlive: Boolean get() = currentHp > 0
}

data class TurnResult(
    val heroState: HeroCombatState,
    val enemy: Enemy,
    val heroDamageDealt: Int,
    val enemyDamageDealt: Int,
    val combatLog: List<String>,
    val isCombatOver: Boolean,
    val heroWon: Boolean,
)

object CombatEngine {

    fun executeTurn(
        hero: HeroCombatState,
        enemy: Enemy,
        action: CombatAction,
    ): TurnResult {
        var currentHero = hero.copy(isDefending = false)
        var currentEnemy = enemy
        val logs = mutableListOf<String>()

        var heroDamage = 0
        var heroEnergyCost = 0

        when (action) {
            is CombatAction.BasicAttack -> {
                heroDamage = currentHero.stats.damage.attack
                logs.add("L'eroe esegue un Attacco Base infliggendo $heroDamage danni.")
            }
            is CombatAction.Defend -> {
                currentHero = currentHero.copy(isDefending = true)
                logs.add("L'eroe assume una posizione difensiva (+${currentHero.stats.survival.defenseReductionPercent}% riduzione danno).")
            }
            is CombatAction.SpecialSkill -> {
                heroEnergyCost = 2
                if (currentHero.currentEnergy >= heroEnergyCost) {
                    val elementBonus = action.element?.let { currentHero.stats.damage.elementBonusPercent(it) } ?: 0
                    heroDamage = currentHero.stats.damage.special + (currentHero.stats.damage.special * elementBonus / 100)
                    logs.add("L'eroe usa Special Skill [${action.element ?: "Fisico"}] infliggendo $heroDamage danni!")
                } else {
                    heroEnergyCost = 0
                    heroDamage = currentHero.stats.damage.attack
                    logs.add("Energia insufficiente! L'eroe ripiega su un Attacco Base ($heroDamage danni).")
                }
            }
            is CombatAction.MagicSpell -> {
                heroEnergyCost = 3
                if (currentHero.currentEnergy >= heroEnergyCost) {
                    val elementBonus = action.element?.let { currentHero.stats.damage.elementBonusPercent(it) } ?: 0
                    heroDamage = currentHero.stats.damage.magic + (currentHero.stats.damage.magic * elementBonus / 100)
                    logs.add("L'eroe lancia una Magia [${action.element ?: "Arcana"}] infliggendo $heroDamage danni ad area!")
                } else {
                    heroEnergyCost = 0
                    heroDamage = currentHero.stats.damage.attack
                    logs.add("Energia insufficiente! L'eroe ripiega su un Attacco Base ($heroDamage danni).")
                }
            }
            is CombatAction.Ultimate -> {
                heroEnergyCost = 5
                if (currentHero.currentEnergy >= heroEnergyCost) {
                    heroDamage = currentHero.stats.damage.ultimate
                    logs.add("ULTIMATE ACTIVATED! L'eroe devasta il nemico per $heroDamage danni colossali!")
                } else {
                    heroEnergyCost = 0
                    heroDamage = currentHero.stats.damage.attack
                    logs.add("Energia insufficiente per l'Ultimate! Attacco Base ($heroDamage danni).")
                }
            }
        }

        currentHero = currentHero.copy(
            currentEnergy = max(0, currentHero.currentEnergy - heroEnergyCost + currentHero.stats.pace.energyPerTurn)
                .coerceAtMost(currentHero.stats.pace.maxEnergy)
        )

        // Applica danno all'enemy
        val newEnemyHp = max(0, currentEnemy.currentHp - heroDamage)
        currentEnemy = currentEnemy.copy(currentHp = newEnemyHp)

        if (!currentEnemy.isAlive) {
            logs.add("${currentEnemy.name} è stato sconfitto!")
            // Fine combattimento: eventuale guarigione fine scontro
            val healPercent = currentHero.stats.survival.endOfFightHealPercent
            if (healPercent > 0) {
                val healAmount = (currentHero.stats.survival.maxHp * healPercent) / 100
                val healedHp = (currentHero.currentHp + healAmount).coerceAtMost(currentHero.stats.survival.maxHp)
                currentHero = currentHero.copy(currentHp = healedHp)
                logs.add("L'eroe recupera $healAmount HP dopo la vittoria ($healedHp/${currentHero.stats.survival.maxHp}).")
            }
            return TurnResult(
                heroState = currentHero,
                enemy = currentEnemy,
                heroDamageDealt = heroDamage,
                enemyDamageDealt = 0,
                combatLog = logs,
                isCombatOver = true,
                heroWon = true,
            )
        }

        // Contrattacco del nemico
        var incomingDamage = currentEnemy.attackDamage
        if (currentHero.isDefending) {
            val reduction = currentHero.stats.survival.defenseReductionPercent
            incomingDamage = max(1, incomingDamage - (incomingDamage * reduction / 100))
        }
        val finalDamage = max(1, incomingDamage - currentHero.stats.survival.armor)
        val newHeroHp = max(0, currentHero.currentHp - finalDamage)
        currentHero = currentHero.copy(currentHp = newHeroHp)
        logs.add("${currentEnemy.name} colpisce l'eroe infliggendo $finalDamage danni.")

        val isOver = !currentHero.isAlive
        if (isOver) {
            logs.add("L'eroe è caduto in battaglia...")
        }

        return TurnResult(
            heroState = currentHero,
            enemy = currentEnemy,
            heroDamageDealt = heroDamage,
            enemyDamageDealt = finalDamage,
            combatLog = logs,
            isCombatOver = isOver,
            heroWon = false,
        )
    }
}
