package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.BlackCard
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BlackCardRepository : CrudRepository<BlackCard, Int> {

    @Query(
        """
            select black_cards.* from black_cards
            where black_cards.id not in (
                select unnest(used_black_cards) from games 
                where games.id = :game_id
            )
            order by black_cards.id
        """
    )
    fun getUnusedBlackCards(@Param("game_id") gameId: Int): List<BlackCard>

}