package dev.vrba.discord.cah.entities

import dev.vrba.discord.cah.entities.refs.SelectedWhiteCardRef
import dev.vrba.discord.cah.entities.refs.WhiteCardRef
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("players")
data class Player(
    @Id
    val id: Int = 0,

    @Column("user_id")
    val user: Long,

    @Column("game_id")
    val game: Int,

    @Column("selected_white_card_id")
    val selectedWhiteCard: Int? = null,

    val score: Int = 0,

    @MappedCollection(idColumn = "id", keyColumn = "id")
    val hand: List<WhiteCardRef> = emptyList(),

    @MappedCollection(idColumn = "id", keyColumn = "id")
    val selectedCards: List<SelectedWhiteCardRef> = emptyList()
)