package com.fearnavegator.warframe

object WarframeOcrParser {
    private fun number(text: String, vararg labels: String): Double? {
        val lines = text.lines().map { it.trim() }
        for (i in lines.indices) {
            if (labels.any { label -> lines[i].contains(label, ignoreCase = true) }) {
                val same = Regex("([0-9]+(?:[.,][0-9]+)?)\\s*%?").findAll(lines[i]).lastOrNull()?.value
                val next = lines.getOrNull(i + 1)?.let { Regex("([0-9]+(?:[.,][0-9]+)?)").find(it)?.value }
                return (same ?: next)?.replace("%", "")?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
            }
        }
        return null
    }

    fun parse(text: String): WeaponStats? {
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }
        val maxIndex = lines.indexOfFirst { it.contains("Nível Máximo", true) || it.contains("Nivel Maximo", true) }
        val name = if (maxIndex > 0) lines[maxIndex - 1].uppercase() else lines.firstOrNull()?.uppercase() ?: return null
        val speed = number(text, "Velocidade de Ataque") ?: return null
        val total = number(text, "Total") ?: return null
        val critChance = (number(text, "Chance Crítica", "Chance Critica") ?: 0.0) / 100.0
        val critMult = number(text, "Dano Crítico", "Dano Critico") ?: 1.0
        val status = (number(text, "Status") ?: 0.0) / 100.0
        val heavy = number(text, "ATAQUE PESADO", "Dano")
        return WeaponStats(name, speed, total, critChance, critMult, status, heavy)
    }
}
