package dev.vrba.discord.cah.discord

fun Long.asUserMention(): String = "<@${this}>"