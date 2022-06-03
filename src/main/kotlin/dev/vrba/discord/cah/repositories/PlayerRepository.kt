package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.Player
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : CrudRepository<Player, Int> {

    @Query("select * from players where players.game_id = :game_id")
    fun findAllByGameId(@Param("game_id") gameId: Int): List<Player>

    @Modifying
    @Query("""
        delete from players 
        where players.user_id in (
            select unnest(games.leaving_players) from games 
            where games.id = :game_id
        )
    """)
    fun deleteLeavingPlayers(@Param("game_id") gameId: Int)
}