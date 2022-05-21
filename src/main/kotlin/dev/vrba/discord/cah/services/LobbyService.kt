package dev.vrba.discord.cah.services

import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.exceptions.EmbeddableException
import dev.vrba.discord.cah.repositories.LobbyRepository
import dev.vrba.discord.cah.services.contract.LobbyServiceInterface
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LobbyService(private val repository: LobbyRepository) : LobbyServiceInterface {

    override fun createLobby(owner: Long, guild: Long, channel: Long, message: Long): Lobby {
        val lobby = Lobby(
            id = 0,
            owner = owner,
            players = setOf(owner),
            guild = guild,
            channel = channel,
            message = message
        )

        // TODO: Decide whether to update the lobby embed here or in the discord event listener

        return repository.save(lobby)
    }

    override fun joinOrLeaveLobby(id: Int, user: Long): Lobby {
        val lobby = repository.findByIdOrNull(id) ?: throw EmbeddableException(
            "Lobby was not found",
            "It might have been cancelled or started. If not, this is probably a bug."
        )

        if (lobby.owner == user) {
            throw EmbeddableException(
                "You are the owner of this lobby",
                "You cannot leave this lobby. If you wish to cancel this game, please click the Cancel button instead."
            )
        }

        val updated = if (lobby.players.contains(user)) {
            lobby.copy(players = lobby.players - user)
        } else {
            lobby.copy(players = lobby.players + user)
        }

        return repository.save(updated)
    }

    override fun startLobby(id: Int, invoker: Long) {
        TODO("Not yet implemented")
    }

    override fun cancelLobby(id: Int, invoker: Long) {
        TODO("Not yet implemented")
    }
}