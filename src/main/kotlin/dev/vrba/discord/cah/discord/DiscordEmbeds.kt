package dev.vrba.discord.cah.discord

import dev.vrba.discord.cah.entities.Lobby
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

}