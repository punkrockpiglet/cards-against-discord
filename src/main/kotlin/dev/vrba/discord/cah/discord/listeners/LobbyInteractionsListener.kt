package dev.vrba.discord.cah.discord.listeners

import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyCancelledEmbed
import dev.vrba.discord.cah.discord.DiscordEmbeds.lobbyEmbed
import dev.vrba.discord.cah.services.LobbyService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import org.springframework.stereotype.Component

@Component
class LobbyInteractionsListener(private val service: LobbyService) : ApplicationEventListener() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        // Only handle events that come from lobby embed buttons
        if (!event.componentId.startsWith("lobby:")) {
            return
        }

        val user = event.user.idLong

        val (action, id) = event.componentId.removePrefix("lobby:").split(":")
        val parsed = id.toInt()

        handleInteractionErrors(event) {
            when (action) {
                "join" -> joinOrLeaveLobby(parsed, user, event)
                "start" -> startLobby(parsed, user, event)
                "cancel" -> cancelLobby(parsed, user, event)
                else -> throw IllegalArgumentException("Invalid lobby button action [${action}] encountered!")
            }
        }
    }

    private fun joinOrLeaveLobby(id: Int, user: Long, event: ButtonInteractionEvent) {
        val lobby = service.joinOrLeaveLobby(id, user)
        val embed = lobbyEmbed(lobby)

        event.editMessageEmbeds(embed).queue()
    }

    private fun startLobby(id: Int, user: Long, event: ButtonInteractionEvent) {
        service.startLobby(id, user)

        event.deferEdit().complete()
        event.message.delete().queue()
    }

    private fun cancelLobby(id: Int, user: Long, event: ButtonInteractionEvent) {
        service.cancelLobby(id, user)

        event.editMessageEmbeds(lobbyCancelledEmbed())
            .setActionRows(listOf())
            .queue()
    }
}