package dev.vrba.discord.cah.discord.listeners

import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyCancelledEmbed
import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyEmbed
import dev.vrba.discord.cah.services.LobbyService
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class LobbyInteractionsListener(private val service: LobbyService) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        // Only handle events that come from lobby embed buttons
        if (!event.componentId.startsWith("lobby:")) {
            return
        }

        val segments = event.componentId.removePrefix("lobby:").split(":")

        val user = event.user.idLong
        val message = event.message

        val action = segments[0]
        val id = segments[1].toInt()

        when (action) {
            "join" -> joinOrLeaveLobby(id, user, message)
            "start" -> startLobby(id, user, message)
            "cancel" -> cancelLobby(id, user, message)
            else -> throw IllegalArgumentException("Invalid lobby button action [${action}] encountered!")
        }
    }

    private fun joinOrLeaveLobby(id: Int, user: Long, message: Message) {
        val lobby = service.joinOrLeaveLobby(id, user)
        val embed = lobbyEmbed(lobby)

        message.editMessageEmbeds(embed).queue()
    }

    private fun startLobby(id: Int, user: Long, message: Message) {
        service.startLobby(id, user)
        message.delete().queue()
    }

    private fun cancelLobby(id: Int, user: Long, message: Message) {
        service.cancelLobby(id, user)
        message.editMessageEmbeds(lobbyCancelledEmbed()).queue()
    }
}