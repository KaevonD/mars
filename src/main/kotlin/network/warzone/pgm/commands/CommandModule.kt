package network.warzone.pgm.commands

import app.ashcon.intake.argument.CommandArgs
import app.ashcon.intake.parametric.AbstractModule
import app.ashcon.intake.parametric.Provider
import net.kyori.adventure.audience.Audience
import network.warzone.pgm.commands.providers.AudienceProvider
import network.warzone.pgm.commands.providers.PlayerContextProvider
import network.warzone.pgm.commands.providers.RankProvider
import network.warzone.pgm.commands.providers.TagProvider
import network.warzone.pgm.player.PlayerContext
import network.warzone.pgm.ranks.models.Rank
import network.warzone.pgm.tags.models.Tag
import java.util.function.Supplier
import kotlin.reflect.KClass

object CommandModule : AbstractModule() {

    override fun configure() {
        bind(Audience::class, AudienceProvider())
        bind(Rank::class, RankProvider())
        bind(Tag::class, TagProvider())
        bind(PlayerContext::class, PlayerContextProvider())
    }

    private fun <T : Any> bind(type: KClass<T>, provider: Provider<T>) {
        bind(type.java).toProvider(provider)
    }

    private fun <T : Any> bind(type: KClass<T>, supplier: Singleton<T>) {
        bind(type, supplier)
    }

    @FunctionalInterface
    private interface Singleton<T> : Provider<T>, Supplier<T> {

        override fun get(args: CommandArgs, list: MutableList<out Annotation>): T {
            return get()
        }

        override fun isProvided(): Boolean {
            return true
        }

    }
}