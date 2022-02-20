package network.warzone.mars.rank.exceptions

import network.warzone.mars.utils.FeatureException
import tc.oc.pgm.lib.net.kyori.adventure.text.Component
import tc.oc.pgm.lib.net.kyori.adventure.text.Component.text
import tc.oc.pgm.lib.net.kyori.adventure.text.Component.translatable
import tc.oc.pgm.lib.net.kyori.adventure.text.format.NamedTextColor

data class RankMissingException(val name: String) : FeatureException() {
    override fun asComponent(): Component {
        return translatable("exception.rank.not-exist", NamedTextColor.RED, text(name))
    }
}
