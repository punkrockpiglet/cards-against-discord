package dev.vrba.discord.cah.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

interface ApplicationSlashCommand {

    fun define(): SlashCommandData

    fun execute(event: SlashCommandInteractionEvent)

}