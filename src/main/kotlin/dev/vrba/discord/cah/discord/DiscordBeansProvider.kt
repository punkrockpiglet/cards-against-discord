package dev.vrba.discord.cah.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DiscordBeansProvider(private val configuration: DiscordConfiguration) {
    @Bean
    fun jda(): JDA {
        return JDABuilder.createDefault(configuration.token)
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.playing("Cards Against Humanity"))
            .build()
    }
}