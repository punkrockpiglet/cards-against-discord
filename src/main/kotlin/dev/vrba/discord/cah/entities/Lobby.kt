package dev.vrba.discord.cah.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("lobbies")
data class Lobby(
    @Id
    val id: Int,

    @Column("owner_id")
    val owner: Long,

    val players: Set<Long>,

    @Column("win_points")
    val points: Int = 10,

    @Column("guild_id")
    val guild: Long,

    @Column("channel_id")
    val channel: Long,

    @Column("message_id")
    val message: Long
)