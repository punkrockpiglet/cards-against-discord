package dev.vrba.discord.cah.discord.commands

import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyEmbed
import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyPlaceholderEmbed
import dev.vrba.discord.cah.exceptions.EmbeddableException
import dev.vrba.discord.cah.services.LobbyService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.stereotype.Component

@Component
class CreateLobbyCommand(private val lobbyService: LobbyService) : ApplicationSlashCommand {

    override fun define(): SlashCommandData {
        return Commands.slash("game", "Create a new game lobby")
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val interaction = event.deferReply(true).complete()

        val guild = event.guild?.idLong ?: throw EmbeddableException("This command can be only used in guilds")
        val channel = event.textChannel.idLong
        val owner = event.user.idLong

        event.textChannel.sendMessageEmbeds(lobbyPlaceholderEmbed()).queue {
            val lobby = lobbyService.createLobby(owner, guild, channel, it.idLong)

            val embed = lobbyEmbed(lobby)
            val components = listOf(
                Button.primary("lobby:join:${lobby.id}", "\uD83D\uDC4B Join / Leave"),
                Button.secondary("lobby:start:${lobby.id}", "\uD83D\uDE0E Start"),
                Button.secondary("lobby:cancel:${lobby.id}", "\uD83D\uDC80 Cancel")
            )

            it.editMessageEmbeds(embed).setActionRow(components).queue()
            interaction.editOriginal("✨ Game lobby created ✨").queue()
        }
    }
}