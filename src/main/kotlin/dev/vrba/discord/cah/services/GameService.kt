package dev.vrba.discord.cah.services

import dev.vrba.discord.cah.entities.Game
import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.entities.Player
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.repositories.GameRepository
import dev.vrba.discord.cah.repositories.PlayerRepository
import dev.vrba.discord.cah.repositories.WhiteCardRepository
import dev.vrba.discord.cah.services.contract.GameServiceInterface
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val whiteCardRepository: WhiteCardRepository
) : GameServiceInterface {

    override fun createGame(lobby: Lobby): Game {
        val entity = Game(guild = lobby.guild, channel = lobby.channel)

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
}