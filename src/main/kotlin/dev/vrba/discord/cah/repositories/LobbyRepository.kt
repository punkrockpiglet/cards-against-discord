package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.Lobby
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LobbyRepository : CrudRepository<Lobby, Int>