package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("white_cards")
data class WhiteCard(
    @Id
    val id: Int,
    val text: String
)