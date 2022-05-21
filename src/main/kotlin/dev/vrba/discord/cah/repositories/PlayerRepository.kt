package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.Player
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : CrudRepository<Player, Int>