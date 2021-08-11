package network.warzone.pgm.api.socket

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.bukkit.event.Event
import kotlin.reflect.full.isSubclassOf

data class ComplexType(val test: String)

sealed class InboundEvent<T>(val eventName: String, val toBukkit: (T) -> Event, val parse: (JsonElement) -> T) {
    object PING : InboundEvent<ComplexType>("PING", ::PingEvent, {
        Gson().fromJson(it, ComplexType::class.java)
    })

    fun call(d: JsonElement) {
        toBukkit(parse(d)).callEvent()
    }
    
    companion object {
        private val map = InboundEvent::class.nestedClasses
            .filter { clazz -> clazz.isSubclassOf(InboundEvent::class) }
            .map { clazz -> clazz.objectInstance }
            .map { clazz -> clazz as InboundEvent<*> }
            .associateBy { value -> value.eventName }

        fun <T> valueOf(eventName: String): InboundEvent<T> {
            if (map.containsKey(eventName)) return map[eventName] as InboundEvent<T>

            throw IllegalArgumentException("No InboundEvent exists by the name $eventName")
        }
    }
}

class PingEvent(val data: ComplexType) : Event()