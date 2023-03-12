package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "index")
class Context : Serializable {
    @get:JsonProperty("uri")
    @set:JsonProperty("uri")
    @JsonProperty("uri")
    var uri: String? = null

    @get:JsonProperty("name")
    @set:JsonProperty("name")
    @JsonProperty("name")
    var name: String? = null

    @get:JsonProperty("index")
    @set:JsonProperty("index")
    @JsonProperty("index")
    var index = 0

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
     * @param index
     * @param uri
     */
    constructor(uri: String?, name: String?, index: Int) : super() {
        this.uri = uri
        this.name = name
        this.index = index
    }

    fun withUri(uri: String?): Context {
        this.uri = uri
        return this
    }

    fun withName(name: String?): Context {
        this.name = name
        return this
    }

    fun withIndex(index: Int): Context {
        this.index = index
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

    fun withAdditionalProperty(name: String, value: Any): Context {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = 2594078876265426287L
    }
}