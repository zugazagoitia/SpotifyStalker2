package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("timestamp", "user", "track")
class UserPlayingInfo : Serializable {
    @get:JsonProperty("timestamp")
    @set:JsonProperty("timestamp")
    @JsonProperty("timestamp")
    var timestamp: Long = 0

    @get:JsonProperty("user")
    @set:JsonProperty("user")
    @JsonProperty("user")
    var user: User? = null

    @get:JsonProperty("track")
    @set:JsonProperty("track")
    @JsonProperty("track")
    var track: Track? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param track
     * @param user
     * @param timestamp
     */
    constructor(timestamp: Long, user: User?, track: Track?) : super() {
        this.timestamp = timestamp
        this.user = user
        this.track = track
    }

    fun withTimestamp(timestamp: Long): UserPlayingInfo {
        this.timestamp = timestamp
        return this
    }

    fun withUser(user: User?): UserPlayingInfo {
        this.user = user
        return this
    }

    fun withTrack(track: Track?): UserPlayingInfo {
        this.track = track
        return this
    }

    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }

    fun withAdditionalProperty(name: String, value: Any): UserPlayingInfo {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = 1020007978776188465L
    }
}