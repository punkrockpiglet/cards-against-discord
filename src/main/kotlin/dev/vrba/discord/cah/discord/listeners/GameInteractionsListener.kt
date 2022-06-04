package dev.vrba.discord.cah.discord.listeners

import dev.vrba.discord.cah.discord.DiscordEmbeds.whiteCardSelectionEmbed
import dev.vrba.discord.cah.exceptions.EmbeddableException
import dev.vrba.discord.cah.services.GameService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class GameInteractionsListener(private val service: GameService) : ApplicationEventListener() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        // Only handle events that come from game embed buttons
        if (!event.componentId.startsWith("game:")) {
            return
        }

        val user = event.user.idLong

        val parts = event.componentId.removePrefix("game:").split(":")

        val action = parts[0]
        val id = parts[1].toInt()
        val parameters = parts.drop(2)

        handleInteractionErrors(event) {
            when (action) {
                "pick" -> displayWhiteCardSelection(event, id, user)
                else -> throw IllegalArgumentException("Invalid game action [$action] encountered!")
            }
        }
    }

    private fun displayWhiteCardSelection(event: ButtonInteractionEvent, id: Int, user: Long) {
        val interaction = event.deferReply(true).complete()

        // TODO: Merge those queries into a single database call?
        val game = service.getGameById(id)
        val player = service.getPlayerByUserId(id, user)
        val blackCard = service.getCurrentBlackCard(id)

        val judge = game.judge ?: throw IllegalStateException("Judge is not set during white card selection!")

        if (user == judge) {
            throw EmbeddableException(
                "You are the judge for this round",
                "Wait for other players to pick their cards"
            )
        }

        val availableWhiteCards = service.getPlayerWhiteCards(id, user)
        val pickedWhiteCards = service.getPickedWhiteCards(id, user)

        val embed = whiteCardSelectionEmbed(blackCard, availableWhiteCards, pickedWhiteCards)

        val button = ActionRow.of(
            Button.primary("game:${game.id}:confirm-selection", "Confirm selection")
                .withDisabled(player.selectedWhiteCard != null)
        )

        val select = ActionRow.of(
            SelectMenu
                .create("game:${game.id}:select")
                .addOptions(availableWhiteCards.map {
                    SelectOption.of(it.text.substring(0, min(it.text.length, 100)), it.id.toString())
                })
                .build()
        )

        interaction
            .editOriginalEmbeds(embed)
            .setActionRows(select, button)
            .queue()
    }

}