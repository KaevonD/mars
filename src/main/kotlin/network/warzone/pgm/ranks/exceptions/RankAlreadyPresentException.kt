package network.warzone.pgm.ranks.exceptions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import network.warzone.pgm.player.PlayerContext
import network.warzone.pgm.ranks.models.Rank
import network.warzone.pgm.utils.FeatureException

data class RankAlreadyPresentException(val player: PlayerContext, val rank: Rank) : FeatureException() {

    override fun asTextComponent(): TextComponent {
        return Component.text("${player.player.name} already has ${rank.name}", NamedTextColor.RED)
    }

}