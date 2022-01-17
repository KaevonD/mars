package network.warzone.mars.api.socket

enum class SocketEventType {
    MATCH_LOAD,
    MATCH_START,
    MATCH_END,
    FORCE_MATCH_END, // plugin bound
    CORE_LEAK,
    DESTROYABLE_DESTROY,
    FLAG_CAPTURE,
    FLAG_PICKUP,
    FLAG_DROP,
    FLAG_DEFEND,
    WOOL_CAPTURE,
    WOOL_PICKUP,
    WOOL_DROP,
    WOOL_DEFEND,
    CONTROL_POINT_CAPTURE,
    PLAYER_DEATH,
    KILLSTREAK,
    PLAYER_CHAT,
    PARTY_JOIN,
    PARTY_LEAVE,
    MESSAGE,
    PLAYER_XP_GAIN,
}
