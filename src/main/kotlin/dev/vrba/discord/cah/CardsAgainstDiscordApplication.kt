package dev.vrba.discord.cah

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CardsAgainstDiscordApplication

fun main(args: Array<String>) {
    runApplication<CardsAgainstDiscordApplication>(*args)
}
