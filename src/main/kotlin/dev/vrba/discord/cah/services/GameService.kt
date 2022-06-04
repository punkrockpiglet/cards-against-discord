package dev.vrba.discord.cah.services

import dev.vrba.discord.cah.discord.DiscordEmbeds.gamePlaceholderEmbed
import dev.vrba.discord.cah.discord.DiscordEmbeds.gameRoundEmbed
import dev.vrba.discord.cah.discord.asUserMention
import dev.vrba.discord.cah.entities.Game
import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.entities.Player
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.entities.refs.WhiteCardRef
import dev.vrba.discord.cah.repositories.BlackCardRepository
import dev.vrba.discord.cah.repositories.GameRepository
import dev.vrba.discord.cah.repositories.PlayerRepository
import dev.vrba.discord.cah.repositories.WhiteCardRepository
import dev.vrba.discord.cah.services.contract.GameServiceInterface
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GameService(
    private val jda: JDA,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val blackCardRepository: BlackCardRepository,
    private val whiteCardRepository: WhiteCardRepository
) : GameServiceInterface {

    override fun createGame(lobby: Lobby): Game {
        val channel = findChannel(lobby.guild, lobby.channel)
            ?: throw IllegalStateException("Cannot find the invoking text channel!")
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
        createNewGameRound(game, message)

        return game
    }

    override fun getPlayerWhiteCards(game: Int, user: Long): List<WhiteCard> {
        return whiteCardRepository.getAvailableWhiteCards(user)
    }

    override fun getPickedWhiteCards(game: Int, user: Long): List<WhiteCard> {
        return whiteCardRepository.getPickedWhiteCards(user)
    }

    private fun createNewGameRound(game: Game, message: Message?) {
        // Remove leaving players from the game and add all new players that are joining
        playerRepository.deleteLeavingPlayers(game.id)
        playerRepository.saveAll(game.joiningPlayers.map { Player(user = it, game = game.id) })

        // Fill in players' hands with missing cards
        val pool = whiteCardRepository.getUnusedWhiteCards(game.id).shuffled()
        val initial = emptyList<Player>() to pool

        // Ensure that each player has 8 cards available at the start of every round
        val (players, _) = playerRepository.findAllByGameId(game.id).fold(initial) { (players, cards), player ->
            val fill = cards.take(8 - player.hand.size)
            val remaining = cards.drop(8 - player.hand.size)
            val updated = player.copy(
                hand = player.hand + fill.map {
                    WhiteCardRef(
                        player = AggregateReference.to(player.id),
                        whiteCard = AggregateReference.to(it.id)
                    )
                }
            )

            players + updated to remaining
        }

        playerRepository.saveAll(players)

        // Select a new black card for the round
        val blackCard = blackCardRepository.getUnusedBlackCards(game.id).shuffled().first()

        // Choose a new card czar for the round as the player that's next after the judge from the previous round
        // If there is no judge set from the previous round, choose a random player
        val previousJudge = players.firstOrNull { it.id == game.judge } ?: players.random()
        val judge = players[(players.indexOf(previousJudge) + 1) % players.size]
        val updated = gameRepository.save(
            game.copy(
                judge = judge.id,
                message = message?.idLong,
                blackCard = blackCard.id,
                usedBlackCards = game.usedBlackCards + listOfNotNull(game.blackCard),
            )
        )

        updateGameEmbed(updated)
    }

    private fun updateGameEmbed(game: Game) {
        val message = findMessage(game.guild, game.channel, game.message ?: 0) ?: throw IllegalStateException("Cannot find the game message!")
        val blackCard = blackCardRepository.findByIdOrNull(game.blackCard ?: 0) ?: throw IllegalStateException("Cannot find the currently configured black card.")
        val allPlayers = playerRepository.findAllByGameId(game.id)

        val judge = allPlayers.first { it.id == game.judge }.user.asUserMention()
        val players = allPlayers
            .filter { it.id != game.judge }
            .map {
                val emoji = if (it.selectedCards.size <= blackCard.blanks) "⏳" else "✅"
                val mention = it.user.asUserMention()

                "$emoji $mention"
            }
            .sorted()

        val embed = gameRoundEmbed(blackCard, judge, players)
        val component = Button.primary("game:pick:${game.id}", "Pick your card${if (blackCard.blanks > 1) "s" else ""}")

        message.editMessageEmbeds(embed)
            .setActionRow(component)
            .complete()
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
