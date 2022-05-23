package dev.vrba.discord.cah.discord.listeners

import dev.vrba.discord.cah.discord.DiscordEmbeds.errorEmbed
import dev.vrba.discord.cah.exceptions.EmbeddableException
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ComponentInteraction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ApplicationEventListener : ListenerAdapter() {

    private val logger: Logger = LoggerFactory.getLogger(this::class.qualifiedName)

    protected fun handleInteractionErrors(event: ComponentInteraction, action: () -> Unit) {
        try {
            action()
        }
        catch (exception: EmbeddableException) {
            replyErrorEmbed(event, exception.toEmbed())
        }
        catch (exception: Throwable) {
            logger.error(exception.message, exception)
            replyErrorEmbed(event, errorEmbed())
        }
    }

    private fun replyErrorEmbed(event: ComponentInteraction, embed: MessageEmbed) {
        if (event.isAcknowledged) {
            event.hook.editOriginalEmbeds(embed).queue()
            return
        }

        event.replyEmbeds(embed)
            .setEphemeral(true)
            .queue()
    }
}