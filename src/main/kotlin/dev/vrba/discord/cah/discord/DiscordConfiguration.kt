package dev.vrba.discord.cah.discord

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "discord")
data class DiscordConfiguration @ConstructorBinding constructor(
    val token: String,
    val developmentGuildId: Long
)