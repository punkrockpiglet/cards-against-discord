package dev.vrba.discord.cah.discord

fun Long.asUserMention(): String = "<@${this}>"

fun String.asUserMention(): String = "<@${this}>"