package dev.vrba.discord.cah.exceptions

open class EmbeddableException(val title: String, val description: String? = null) : RuntimeException()