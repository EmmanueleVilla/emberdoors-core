package com.shadowings.emberdoors.model.combat

import com.shadowings.emberdoors.model.Element

sealed interface CombatAction {
    data object BasicAttack : CombatAction
    data object Defend : CombatAction
    data class SpecialSkill(val element: Element? = null) : CombatAction
    data class MagicSpell(val element: Element? = null) : CombatAction
    data object Ultimate : CombatAction
}
