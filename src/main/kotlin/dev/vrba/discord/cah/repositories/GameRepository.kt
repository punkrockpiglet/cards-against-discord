package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.Game
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository<Game, Int>