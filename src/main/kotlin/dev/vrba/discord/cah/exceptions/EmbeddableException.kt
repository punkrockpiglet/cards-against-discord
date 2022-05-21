package dev.vrba.discord.cah.exceptions

class EmbeddableException(val title: String, val description: String? = null) : RuntimeException()