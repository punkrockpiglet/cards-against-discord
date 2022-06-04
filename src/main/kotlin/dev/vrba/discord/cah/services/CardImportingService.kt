package dev.vrba.discord.cah.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vrba.discord.cah.entities.BlackCard
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.repositories.BlackCardRepository
import dev.vrba.discord.cah.repositories.WhiteCardRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class CardImportingService(
    private val whiteCardRepository: WhiteCardRepository,
    private val blackCardRepository: BlackCardRepository,
    private val mapper: ObjectMapper,
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)

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
        val cards = this::class.java.getResourceAsStream("/cards.json")
            ?.let { mapper.readValue<CardsAgainstJsonSource>(it) }
            ?: throw IllegalStateException("Cannot find the /cards.json source file!")

        whiteCardRepository.deleteAll()
        blackCardRepository.deleteAll()

        whiteCardRepository.saveAll(cards.white.map { WhiteCard(0, it.replace("\\n", "\n")) })
        blackCardRepository.saveAll(cards.black.map { BlackCard(0, it.pick, it.text.replace("\\n", "\n")) })

        logger.info("Imported ${cards.white.size} white cards and ${cards.black.size} black cards")
    }
}