package dev.vrba.discord.cah.discord

import dev.vrba.discord.cah.entities.BlackCard
import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.entities.WhiteCard
import dev.vrba.discord.cah.exceptions.EmbeddableException
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

object DiscordEmbeds {

    const val thumbnail = "https://i.imgur.com/0imOaFr.png"

    private const val thumbnailDisabled = "https://i.imgur.com/8h3IkgG.png"

    fun exceptionEmbed(exception: EmbeddableException): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.red)
            .setTitle(exception.title)
            .setDescription(exception.description ?: "No further details were provided")
            .build()
    }
    
    fun errorEmbed(error: String? = null): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.red)
            .setTitle(error ?: "Sorry, there was an error")
            .build()
    }

    fun lobbyPlaceholderEmbed(): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.primary)
            .setTitle("Creating a new game lobby...")
            .setDescription("This shouldn't take long")
            .build()
    }

    fun lobbyCancelledEmbed(): MessageEmbed {
        return EmbedBuilder()
            .setTitle("This game was cancelled")
            .setDescription("To create a new lobby, use the **/game** command")
            .setThumbnail(thumbnailDisabled)
            .build()
    }

    fun lobbyEmbed(lobby: Lobby): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.primary)
            .setTitle("Let's play Cards Against Humanity!")
            .setDescription(
                """
                To join the game, click the blue button below the message.
                You can leave the game by clicking the button again.
                """.trimIndent()
            )
            .addField("Game owner", lobby.owner.asUserMention(), false)
            .addField("Joined players", lobby.players.joinToString(", ") { it.asUserMention() }, false)
            .setFooter("First player to reach ${lobby.points} points wins the game")
            .setThumbnail(thumbnail)
            .build()
    }

    fun gamePlaceholderEmbed(): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.primary)
            .setTitle("Starting a new game...")
            .setDescription("This shouldn't take long")
            .build()
    }

    fun gameRoundEmbed(blackCard: BlackCard, judge: String, players: List<String>): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.primary)
            .setTitle(blackCard.formatText())
            .setDescription("To pick your white card${if (blackCard.blanks > 1) "s" else ""} press the button below this message")
            .addField("The judge", "⚖️ $judge", false)
            .addField("Players", players.joinToString("\n"), false)
            .build()
    }

    fun whiteCardSelectionEmbed(blackCard: BlackCard, availableWhiteCards: List<WhiteCard>, pickedWhiteCards: List<WhiteCard>): MessageEmbed {
        val text = blackCard.formatText(pickedWhiteCards.map { it.text })
        val cards = availableWhiteCards.joinToString("\n") { it.text }

        return EmbedBuilder()
            .setColor(DiscordColors.fuchsia)
            .setTitle("Pick a white card to fill in the blanks")
            .setDescription(text)
            .addField("Available cards", cards, false)
            .build()
    }
}