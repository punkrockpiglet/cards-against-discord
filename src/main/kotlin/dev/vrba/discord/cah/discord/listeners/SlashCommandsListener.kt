package dev.vrba.discord.cah.discord.listeners

import dev.vrba.discord.cah.discord.DiscordEmbeds
import dev.vrba.discord.cah.discord.commands.ApplicationSlashCommand
import dev.vrba.discord.cah.exceptions.EmbeddableException
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class SlashCommandsListener(private val commands: List<ApplicationSlashCommand>) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val handler = commands.firstOrNull { it.define().name == event.name }
            ?: throw IllegalStateException(
                """
                Command handler not found for /${event.name}! 
                Perhaps calling an invalid cached command?"
                """.trimIndent()
            )

        try {
            handler.execute(event)
        }
        catch (exception: Exception) {
            val embed = if (exception is EmbeddableException) exception.toEmbed()
                        else DiscordEmbeds.errorEmbed(exception.message)

            if (event.isAcknowledged) {
                event.hook.editOriginalEmbeds(embed).queue()
                return
            }

            event.replyEmbeds(embed)
                .setEphemeral(true)
                .queue()
        }
    }

}