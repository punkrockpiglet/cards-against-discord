package dev.vrba.discord.cah.entities.refs

import dev.vrba.discord.cah.entities.Player
import dev.vrba.discord.cah.entities.WhiteCard
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("picked_cards")
data class SelectedWhiteCardRef(
    @Id
    val id: Int = 0, // Spring data JDBC does not support composite primary keys yet...

    @Column("player_id")
    val player: AggregateReference<Player, Int>,

    @Column("white_card_id")
    val whiteCard: AggregateReference<WhiteCard, Int>,

    @Column("blank_position")
    val position: Int = 0,
)
