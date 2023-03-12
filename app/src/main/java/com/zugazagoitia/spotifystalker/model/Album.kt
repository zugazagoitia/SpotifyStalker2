package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name")
class Album : Serializable {
    @get:JsonProperty("uri")
    @set:JsonProperty("uri")
    @JsonProperty("uri")
    var uri: String? = null

    @get:JsonProperty("name")
    @set:JsonProperty("name")
    @JsonProperty("name")
    var name: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param name
     * @param uri
     */
    constructor(uri: String?, name: String?) : super() {
        this.uri = uri
        this.name = name
    }

    fun withUri(uri: String?): Album {
        this.uri = uri
        return this
    }

    fun withName(name: String?): Album {
        this.name = name
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

    fun withAdditionalProperty(name: String, value: Any): Album {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = 3309449019202775216L
    }
}