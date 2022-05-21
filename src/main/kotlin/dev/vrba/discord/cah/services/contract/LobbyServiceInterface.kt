package dev.vrba.discord.cah.services.contract

import dev.vrba.discord.cah.entities.Lobby

interface LobbyServiceInterface {

    fun createLobby(owner: Long, guild: Long, channel: Long, message: Long): Lobby

    fun joinOrLeaveLobby(id: Int, user: Long): Lobby

    fun startLobby(id: Int, invoker: Long)

    fun cancelLobby(id: Int, invoker: Long)

}