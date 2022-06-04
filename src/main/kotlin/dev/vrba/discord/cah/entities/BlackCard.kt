package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("black_cards")
data class BlackCard(
    @Id
    val id: Int,
    val blanks: Int,
    val text: String
) {
    fun formatText(whiteCards: List<String>? = null): String {
        val text =  text.replace("\\n", "\n")

        val pattern = "_+".toRegex()
        val replacement = "`________`"

        // If there are no fills in provided (e.g. for the new round embed creation) just highlight the blanks
        if (whiteCards == null) {
            return text.replace(pattern, replacement)
        }

        // If there are no blank spaces on the black card, append one at the end
        // Typical situations where this occurs are when the black card text is a question
        val original = if (text.contains("_")) text else "$text\n_"

        return whiteCards
            .fold(original) { result, card -> result.replaceFirst(pattern, "**$card**") }
            .replace(pattern, replacement)
    }
}