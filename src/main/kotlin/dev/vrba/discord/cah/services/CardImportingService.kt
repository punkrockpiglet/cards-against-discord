package dev.vrba.discord.cah.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vrba.discord.cah.entities.BlackCard
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.repositories.BlackCardRepository
import dev.vrba.discord.cah.repositories.WhiteCardRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class CardImportingService(
    private val whiteCardRepository: WhiteCardRepository,
    private val blackCardRepository: BlackCardRepository,
    private val mapper: ObjectMapper,
) : CommandLineRunner {


    data class BlackCardSource(
        val text: String,
        val pick: Int
    )

    data class CardsAgainstJsonSource(
        val white: List<String>,
        val black: List<BlackCardSource>,
        val metadata: Any
    )

    override fun run(vararg args: String?) {
        val source = this::class.java.getResourceAsStream("/cards.json") ?: throw IllegalStateException("Cannot find the /cards.json source file!")
        val cards = mapper.readValue<CardsAgainstJsonSource>(source)

        whiteCardRepository.deleteAll()
        blackCardRepository.deleteAll()

        whiteCardRepository.saveAll(cards.white.map { WhiteCard(0, it) })
        blackCardRepository.saveAll(cards.black.map { BlackCard(0, it.pick, it.text) })
    }
}