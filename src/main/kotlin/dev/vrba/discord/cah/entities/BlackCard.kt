package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("black_cards")
data class BlackCard(
    @Id
    val id: Int,
    val blanks: Int,
    val text: String
)