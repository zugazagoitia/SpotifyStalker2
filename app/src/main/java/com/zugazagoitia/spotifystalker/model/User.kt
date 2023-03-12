package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "imageUrl")
class User : Serializable {
    @get:JsonProperty("uri")
    @set:JsonProperty("uri")
    @JsonProperty("uri")
    var uri: String? = null

    @get:JsonProperty("name")
    @set:JsonProperty("name")
    @JsonProperty("name")
    var name: String? = null

    @get:JsonProperty("imageUrl")
    @set:JsonProperty("imageUrl")
    @JsonProperty("imageUrl")
    var imageUrl: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param imageUrl
     * @param name
     * @param uri
     */
    constructor(uri: String?, name: String?, imageUrl: String?) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
    }

    fun withUri(uri: String?): User {
        this.uri = uri
        return this
    }

    fun withName(name: String?): User {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): User {
        this.imageUrl = imageUrl
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

    fun withAdditionalProperty(name: String, value: Any): User {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = 772954245308809432L
    }
}