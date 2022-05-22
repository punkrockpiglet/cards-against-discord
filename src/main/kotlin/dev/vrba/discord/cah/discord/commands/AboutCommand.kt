package dev.vrba.discord.cah.discord.commands

import dev.vrba.discord.cah.discord.DiscordColors
import dev.vrba.discord.cah.exceptions.EmbeddableException
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.stereotype.Component

@Component
class AboutCommand : ApplicationSlashCommand {

    override fun define(): SlashCommandData {
        return Commands.slash("about", "Provides information about this bot")
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        // Maybe extract this to DiscordEmbeds object?
        val embed = EmbedBuilder()
            .setColor(DiscordColors.primary)
            .setTitle("Cards Against Discord")
            .setThumbnail("https://i.imgur.com/f4B2lMc.png")
            .setDescription("**The ultimate Cards Against Humanity bot.**")
            .setFooter("This bot is playing on ${event.jda.guilds.size} servers already!")
            .build()
            // TODO: Add a proper README

        val components = listOf(
            Button.link("https://discord.gg/a5ZBRnFsu7", "Support server"),
            Button.link("https://github.com/jirkavrba/cards-against-discord", "GitHub repository"),
            Button.link("https://top.gg/bot/932398039862243358", "Rate this bot on top.gg"),
        )

        event.replyEmbeds(embed)
            .addActionRow(components)
            .queue()
    }
}