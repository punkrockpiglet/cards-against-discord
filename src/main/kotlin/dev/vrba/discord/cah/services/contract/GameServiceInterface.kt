package dev.vrba.discord.cah.services.contract

import dev.vrba.discord.cah.entities.Game
import dev.vrba.discord.cah.entities.Lobby
import dev.vrba.discord.cah.entities.WhiteCard

interface GameServiceInterface {

    fun createGame(lobby: Lobby): Game

    fun getPlayerWhiteCards(game: Int, user: Long): List<WhiteCard>

    fun getPickedWhiteCards(game: Int, user: Long): List<WhiteCard>

    // TODO: More methods to come...
}