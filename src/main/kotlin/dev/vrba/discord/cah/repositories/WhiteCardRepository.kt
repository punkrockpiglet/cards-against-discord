package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.WhiteCard
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface WhiteCardRepository : CrudRepository<WhiteCard, Int> {

    @Query(
        """
            select white_cards.* from white_cards
            where white_cards.id not in (select unnest(used_white_cards) from games where games.id = :game_id) and
                  white_cards.id not in (select white_card_id from player_cards 
                    left join players on player_cards.player_id = players.id 
                    where players.game_id = :game_id
                  )
            order by white_cards.id
        """
    )
    fun getUnusedWhiteCards(@Param("game_id") game: Int): List<WhiteCard>

    @Query(
        """
            select white_cards.* from white_cards
                left join player_cards on white_cards.id = player_cards.white_card_id
                left join players on player_cards.player_id = players.id
            where players.user_id = :user_id
            order by white_cards.id
        """
    )
    fun getAvailableWhiteCards(@Param("user_id") user: Long): List<WhiteCard>

    @Query(
        """
            select white_cards.* from white_cards
                left join picked_cards on white_cards.id = picked_cards.white_card_id
                left join players on picked_cards.player_id = players.id
            where players.user_id = :user_id
            order by picked_cards.blank_index
        """
    )
    fun getPickedWhiteCards(@Param("user_id") user: Long): List<WhiteCard>
}