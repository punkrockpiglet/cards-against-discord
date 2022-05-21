package dev.vrba.discord.cah.exceptions

object LobbyNotFoundException : EmbeddableException(
    "Lobby was not found",
    "It might have been cancelled or started. If not, please file an issue on GitHub."
)