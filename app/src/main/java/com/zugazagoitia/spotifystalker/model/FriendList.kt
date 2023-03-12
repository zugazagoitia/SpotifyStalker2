package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("profiles")
class FriendList : Serializable {
    @get:JsonProperty("profiles")
    @set:JsonProperty("profiles")
    @JsonProperty("profiles")
    var profiles: List<Profile> = ArrayList()

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param profiles
     */
    constructor(profiles: List<Profile>) : super() {
        this.profiles = profiles
    }

    fun withProfiles(profiles: List<Profile>): FriendList {
        this.profiles = profiles
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

    fun withAdditionalProperty(name: String, value: Any): FriendList {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = -4839877586673224580L
    }
}