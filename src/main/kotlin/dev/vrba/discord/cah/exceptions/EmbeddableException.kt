package dev.vrba.discord.cah.exceptions

import dev.vrba.discord.cah.discord.DiscordEmbeds
import net.dv8tion.jda.api.entities.MessageEmbed

open class EmbeddableException(val title: String, val description: String? = null) : RuntimeException(title) {

    fun toEmbed(): MessageEmbed {
        return DiscordEmbeds.exceptionEmbed(this)
    }

}