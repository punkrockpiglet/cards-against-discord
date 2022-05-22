package dev.vrba.discord.cah.discord

import dev.vrba.discord.cah.exceptions.EmbeddableException
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

object DiscordEmbeds {

    fun exceptionEmbed(exception: EmbeddableException): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.red)
            .setTitle(exception.title)
            .setDescription(exception.description ?: "No further details were provided")
            .setFooter("If you think this is a bug, please file an issue on GitHub")
            .build()
    }
    
    fun errorEmbed(error: String? = null): MessageEmbed {
        return EmbedBuilder()
            .setColor(DiscordColors.red)
            .setTitle(error ?: "Sorry, there was an error")
            .setFooter("If you think this is a bug, please file an issue on GitHub")
            .build()
    }

}