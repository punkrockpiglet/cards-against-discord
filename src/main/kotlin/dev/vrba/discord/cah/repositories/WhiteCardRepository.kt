package dev.vrba.discord.cah.repositories

import dev.vrba.discord.cah.entities.WhiteCard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WhiteCardRepository : CrudRepository<WhiteCard, Int>