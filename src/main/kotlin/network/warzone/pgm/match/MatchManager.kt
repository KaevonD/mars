package network.warzone.pgm.match

import io.ktor.util.reflect.*
import network.warzone.pgm.WarzonePGM
import network.warzone.pgm.match.tracker.*
import org.bukkit.event.Listener
import tc.oc.pgm.api.match.Match
import kotlin.reflect.KClass

object MatchManager {

    public val trackers: List<Listener> = listOf(
        CoreTracker(),
        MatchTracker(),
        PlayerTracker(),
        WoolTracker(),
        FlagTracker(),
        DestroyableTracker(),
        ControlPointTracker(),
        ChatTracker(),
        BigStatsTracker()
    )

    inline fun <reified T : Listener> getTracker(tracker: KClass<T>): T? {
        return trackers.find { it.instanceOf(tracker) } as T?
    }

    lateinit var match: Match

    fun init() {
        trackers.forEach(WarzonePGM::registerEvents)
    }

}