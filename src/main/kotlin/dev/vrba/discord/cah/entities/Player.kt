package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("players")
data class Player(
    @Id
    val id: Int,

    @Column("user_id")
    val user: Long,

    @Column("game_id")
    val game: Int,

    @Column("selected_white_card_id")
    val selectedWhiteCard: Int? = null,

    val score: Int = 0
)