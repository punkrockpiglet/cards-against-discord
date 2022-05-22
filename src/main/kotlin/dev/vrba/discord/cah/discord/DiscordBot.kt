package dev.vrba.discord.cah.discord

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.EventListener
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class DiscordBot(
    private val configuration: DiscordConfiguration,
    private val listeners: List<EventListener>
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        JDABuilder.createDefault(configuration.token)
            .addEventListeners(*listeners.toTypedArray())
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.playing("Cards Against Humanity"))
            .build()
    }

}