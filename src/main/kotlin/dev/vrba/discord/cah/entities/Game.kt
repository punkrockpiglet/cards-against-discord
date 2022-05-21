package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("games")
data class Game(
    @Id
    val id: Int,

    @Column("guild_id")
    val guild: Long,

    @Column("channel_id")
    val channel: Long,

    @Column("message_id")
    val message: Long? = null,

    @Column("black_card_id")
    val blackCard: Int? = null,

    @Column("judge_id")
    val judge: Int? = null,

    @Column("used_white_cards")
    val usedWhiteCards: List<Int> = emptyList(),

    @Column("used_black_cards")
    val usedBlackCards: List<Int> = emptyList()
)