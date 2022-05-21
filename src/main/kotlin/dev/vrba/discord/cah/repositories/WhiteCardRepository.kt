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
       select * from white_cards
       where white_cards.id not in (select unnest(used_white_cards) from games where games.id = :game_id) and
             white_cards.id not in (
                select white_card_id from player_cards 
                    left join players on player_cards.player_id = players.id
                where players.game_id = :game_id
             )
       """
    )
    fun getAvailableWhiteCards(@Param("game_id") game: Int): Set<WhiteCard>

}