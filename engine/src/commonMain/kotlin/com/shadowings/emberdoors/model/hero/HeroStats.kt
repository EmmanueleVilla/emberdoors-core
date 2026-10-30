package com.shadowings.emberdoors.model.hero

import com.shadowings.emberdoors.model.Element

/**
 * Hero characteristics: the numbers boons and permanent upgrades can modify.
 *
 * The hero never levels up during a run. Defaults are the GDD base values,
 * placeholders until balancing. Percentages are whole numbers (50 = 50%).
 */
data class HeroStats(
    val survival: SurvivalStats = SurvivalStats(),
    val damage: DamageStats = DamageStats(),
    val pace: PaceStats = PaceStats(),
    val status: StatusStats = StatusStats(),
    val elements: ElementStats = ElementStats(),
)

data class SurvivalStats(
    val maxHp: Int = 50,
    /** Flat value subtracted from every hit taken (minimum 1 damage). */
    val armor: Int = 0,
    /** Damage reduction when choosing Defense. */
    val defenseReductionPercent: Int = 50,
    /** Perfect parry counterattack, as a share of the damage that would have been taken. */
    val parryCounterPercent: Int = 100,
    /** Healing at the end of each fight, as a share of max HP. */
    val endOfFightHealPercent: Int = 0,
)

data class DamageStats(
    val attack: Int = 6,
    val special: Int = 10,
    /** The target plus its neighbours. */
    val specialTargets: Int = 2,
    /** Damage dealt to every enemy. */
    val magic: Int = 5,
    val ultimate: Int = 25,
    /** Bonus damage per element; a missing element means 0%. */
    val elementBonusPercent: Map<Element, Int> = emptyMap(),
) {
    fun elementBonusPercent(element: Element): Int = elementBonusPercent[element] ?: 0
}

data class PaceStats(
    /** Turn order position: higher acts first, the hero wins ties. */
    val speed: Int = 5,
    /** Every fight starts with full energy. */
    val maxEnergy: Int = 5,
    val energyPerTurn: Int = 1,
    val energyPerAttack: Int = 0,
    val specialEnergyCost: Int = 5,
    /** Turns before Magic is available again after use. */
    val magicCooldownTurns: Int = 3,
    val ultimatePointsRequired: Int = 8,
    val ultimatePointsPerOffensiveAction: Int = 1,
    val ultimatePointsPerHitTaken: Int = 1,
    val ultimatePointsPerPerfectParry: Int = 2,
)

data class StatusStats(
    /** Extra turns added to every buff the hero applies to itself. */
    val selfBuffDurationBonusTurns: Int = 0,
    /** Extra turns added to every debuff the hero applies to enemies. */
    val enemyDebuffDurationBonusTurns: Int = 0,
    /** Multiplies status damage per turn. */
    val statusPotencyPercent: Int = 100,
)

data class ElementStats(
    /** Damage resistance per element; a missing element means 0%. */
    val resistancePercent: Map<Element, Int> = emptyMap(),
) {
    fun resistancePercent(element: Element): Int = resistancePercent[element] ?: 0
}
