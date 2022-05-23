package dev.vrba.discord.cah.services

import dev.vrba.discord.cah.discord.DiscordEmbeds.gamePlaceholderEmbed
import dev.vrba.discord.cah.entities.Game
import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.entities.Player
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.repositories.GameRepository
import dev.vrba.discord.cah.repositories.PlayerRepository
import dev.vrba.discord.cah.repositories.WhiteCardRepository
import dev.vrba.discord.cah.services.contract.GameServiceInterface
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import org.springframework.stereotype.Service

@Service
class GameService(
    private val jda: JDA,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val whiteCardRepository: WhiteCardRepository
) : GameServiceInterface {

    override fun createGame(lobby: Lobby): Game {
        val channel = findChannel(lobby.guild, lobby.channel) ?: throw IllegalStateException("Cannot find the invoking text channel!")
        val message = channel.sendMessageEmbeds(gamePlaceholderEmbed()).complete()

        val entity = Game(
            guild = lobby.guild,
            channel = lobby.channel,
            message = message.idLong,
            points = lobby.points
        )

        val game = gameRepository.save(entity)
        val players = lobby.players.map { Player(user = it, game = game.id) }

        playerRepository.saveAll(players)

        return game
    }

    override fun getPlayerWhiteCards(game: Int, user: Long): List<WhiteCard> {
        return whiteCardRepository.getAvailableWhiteCards(user)
    }

    override fun getPickedWhiteCards(game: Int, user: Long): List<WhiteCard> {
        return whiteCardRepository.getPickedWhiteCards(user)
    }

    private fun findMessage(guild: Long, channel: Long, message: Long): Message? {
       return findChannel(guild, channel)
           ?.retrieveMessageById(message)
           ?.complete()
    }

    private fun findChannel(guild: Long, channel: Long): TextChannel? {
        return jda.getGuildById(guild)?.getTextChannelById(channel)
    }
}