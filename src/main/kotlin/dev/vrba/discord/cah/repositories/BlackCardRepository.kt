package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.BlackCard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackCardRepository : CrudRepository<BlackCard, Int>