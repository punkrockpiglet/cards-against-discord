package dev.vrba.discord.cah.services

import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.exceptions.EmbeddableException
import dev.vrba.discord.cah.exceptions.LobbyNotFoundException
import dev.vrba.discord.cah.repositories.LobbyRepository
import dev.vrba.discord.cah.services.contract.LobbyServiceInterface
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LobbyService(private val repository: LobbyRepository) : LobbyServiceInterface {

    override fun createLobby(points: Int, owner: Long, guild: Long, channel: Long, message: Long): Lobby {
        val lobby = Lobby(
            id = 0,
            owner = owner,
            players = setOf(owner),
            points = points,
            guild = guild,
            channel = channel,
            message = message
        )

        return repository.save(lobby)
    }

    override fun joinOrLeaveLobby(id: Int, user: Long): Lobby {
        val lobby = repository.findByIdOrNull(id) ?: throw LobbyNotFoundException

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
        val lobby = repository.findByIdOrNull(id) ?: throw LobbyNotFoundException

        if (lobby.owner != invoker) {
            throw EmbeddableException(
                "You are not the owner of this lobby",
                "Only the owner is eligible to start the game."
            )
        }

        if (lobby.players.size < 2) {
            throw EmbeddableException(
                "There need to be at least 2 players to start the game",
                "Other members can join using the **Join / Leave** button below the lobby embed."
            )
        }

        TODO("Not yet implemented")
    }

    override fun cancelLobby(id: Int, invoker: Long) {
        val lobby = repository.findByIdOrNull(id) ?: throw LobbyNotFoundException

        if (lobby.owner != invoker) {
            throw EmbeddableException(
                "You are not the owner of this lobby",
                "Only the owner is eligible to cancel the game."
            )
        }

        repository.delete(lobby)
    }
}