package network.warzone.mars.map

import network.warzone.mars.api.ApiClient
import network.warzone.mars.feature.NamedCachedFeature
import network.warzone.mars.map.models.GameMap
import network.warzone.mars.map.models.MapContributor
import tc.oc.pgm.api.PGM
import tc.oc.pgm.api.map.Contributor
import tc.oc.pgm.api.map.MapInfo
import tc.oc.pgm.map.contrib.PlayerContributor
import tc.oc.pgm.util.Version
import java.util.*

object MapFeature : NamedCachedFeature<GameMap>() {
    override suspend fun init() {
        findNewMaps()
    }

    override suspend fun fetch(target: String): GameMap? {
        return try {
            ApiClient.get("/mc/maps/$target")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun create(maps: List<MapLoadOneRequest>): List<GameMap> {
        val created = ApiClient.post<List<GameMap>, List<MapLoadOneRequest>>("/mc/maps", maps)

        return created.onEach { add(it) }
    }

    suspend fun list(): List<GameMap> {
        val maps = ApiClient.get<List<GameMap>>("/mc/maps")

        return maps.onEach { add(it) }
    }

    /**
     * Find and load all new maps.
     */
    private suspend fun findNewMaps() {
        // Get the currently loaded maps.
        val currentMaps: List<GameMap> = list()

        // Get the PGM Map Library.
        val pgmMaps = PGM.get().mapLibrary.maps

        // Initialize an empty array of map load requests.
        val mapLoadRequests = mutableListOf<MapLoadOneRequest>()

        // Loop over the iterator of maps.
        while (pgmMaps.hasNext()) {
            // Get the map.
            val map = pgmMaps.next()

            // Try and find a currently loaded map by the same name.
            val existingMap = currentMaps.find { it.name == map.name }

            // If it is currently loaded, check its version.
            if (existingMap != null) {
                // Get the current maps version components.
                val components = existingMap.version.split(".").map { it.toInt() }
                val existingVersion: Version = if (components.size == 3) {
                    val (major, minor, patch) = components
                    Version(major, minor, patch)
                } else {
                    val (major, minor) = components
                    Version(major, minor, 0)
                }

                // Load the map if the existing version is older than the new version.
                if (existingVersion.isOlderThan(map.version)) mapLoadRequests.add(
                    toMapLoadRequest(
                        map,
                        existingMap._id
                    )
                )
            } else {
                // Else, convert the map into a map load request and add it the list.
                mapLoadRequests.add(toMapLoadRequest(map, null))
            }
        }

        // Send all the map load requests to the API.
        create(maps = mapLoadRequests)
    }

    private fun toMapLoadRequest(map: MapInfo, id: UUID?): MapLoadOneRequest {
        return MapLoadOneRequest(
            _id = id ?: UUID.randomUUID(),
            name = map.name,
            version = map.version.toString(),
            gamemodes = map.gamemodes.map { it.name },
            authors = map.authors.mapNotNull { resolveContributor(it) },
            contributors = map.contributors.mapNotNull { resolveContributor(it) }
        )
    }

    private fun resolveContributor(contributor: Contributor): MapContributor? {
        return when (contributor) {
            is PlayerContributor -> MapContributor(contributor.id, contributor.contribution)
            else -> null
        }
    }

    data class MapLoadOneRequest(
        val _id: UUID,
        val name: String,
        val version: String,
        val gamemodes: List<String>,
        val authors: List<MapContributor>,
        val contributors: List<MapContributor>
    )
}