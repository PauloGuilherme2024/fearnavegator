package com.fearnavegator.warframe

import kotlin.math.max

data class WeaponStats(
    val name: String,
    val attackSpeed: Double,
    val totalDamage: Double,
    val criticalChance: Double,
    val criticalMultiplier: Double,
    val statusChance: Double,
    val heavyDamage: Double? = null,
    val stanceHitsPerCycle: Double = 1.0,
    val stanceCycleSeconds: Double = 1.0
)

data class DamageResult(val damagePerMinute: Double, val heavyExpectedDamage: Double?, val note: String?)

object DamageEngine {
    /*
     * Comparison metric, not enemy-specific TTK.
     * Expected damage/hit = totalDamage * E[critical multiplier].
     * E[crit] = 1 + critChance * (critMultiplier - 1), supporting >100% crit continuously.
     * Attack throughput is corrected by stance cycle metadata when available.
     * Status is deliberately not converted into fictional universal damage: proc damage depends
     * on damage type, target and duration. It is surfaced only as a note.
     */
    fun calculate(s: WeaponStats): DamageResult {
        val expectedCrit = 1.0 + max(0.0, s.criticalChance) * max(0.0, s.criticalMultiplier - 1.0)
        val baseHitsPerSecond = if (s.stanceCycleSeconds > 0) s.stanceHitsPerCycle / s.stanceCycleSeconds else 1.0
        val hitsPerSecond = baseHitsPerSecond * s.attackSpeed
        val dpm = s.totalDamage * expectedCrit * hitsPerSecond * 60.0
        val heavy = s.heavyDamage?.times(expectedCrit)
        val note = when {
            s.statusChance >= .40 -> "Status muito alto"
            s.criticalChance >= .30 -> "Crítico muito alto"
            else -> null
        }
        return DamageResult(dpm, heavy, note)
    }
}
