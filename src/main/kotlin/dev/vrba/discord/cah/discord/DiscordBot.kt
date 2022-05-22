package dev.vrba.discord.cah.discord

import dev.vrba.discord.cah.discord.commands.ApplicationSlashCommand
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.EventListener
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Service

@Service
class DiscordBot(
    private val environment: Environment,
    private val configuration: DiscordConfiguration,
    private val listeners: List<EventListener>,
    private val commands: List<ApplicationSlashCommand>
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val jda = JDABuilder.createDefault(configuration.token)
            .addEventListeners(*listeners.toTypedArray())
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.playing("Cards Against Humanity"))
            .build()


        // If running in the development mode, register all slash commands to the configured guild
        // When registering commands globally, there is up to one-hour delay before the commands become available
        if (environment.acceptsProfiles(Profiles.of("development"))) {
            val guild = jda.getGuildById(configuration.developmentGuildId) ?: throw RuntimeException("Cannot find the configured development guild!")

            return guild.updateCommands()
                .addCommands(commands.map { it.define() })
                .queue()
        }

        jda.updateCommands()
            .addCommands(commands.map { it.define() })
            .queue()
    }

}