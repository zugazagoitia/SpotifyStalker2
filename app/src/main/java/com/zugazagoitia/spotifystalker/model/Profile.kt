package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "image_url", "followers_count", "following_count")
class Profile : Serializable {
    @get:JsonProperty("uri")
    @set:JsonProperty("uri")
    @JsonProperty("uri")
    var uri: String? = null

    @get:JsonProperty("name")
    @set:JsonProperty("name")
    @JsonProperty("name")
    var name: String? = null

    @get:JsonProperty("image_url")
    @set:JsonProperty("image_url")
    @JsonProperty("image_url")
    var imageUrl: String? = null

    @get:JsonProperty("followers_count")
    @set:JsonProperty("followers_count")
    @JsonProperty("followers_count")
    var followersCount = 0

    @get:JsonProperty("following_count")
    @set:JsonProperty("following_count")
    @JsonProperty("following_count")
    var followingCount = 0

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
     * @param followersCount
     * @param uri
     * @param followingCount
     */
    constructor(
        uri: String?,
        name: String?,
        imageUrl: String?,
        followersCount: Int,
        followingCount: Int
    ) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
        this.followersCount = followersCount
        this.followingCount = followingCount
    }

    fun withUri(uri: String?): Profile {
        this.uri = uri
        return this
    }

    fun withName(name: String?): Profile {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): Profile {
        this.imageUrl = imageUrl
        return this
    }

    fun withFollowersCount(followersCount: Int): Profile {
        this.followersCount = followersCount
        return this
    }

    fun withFollowingCount(followingCount: Int): Profile {
        this.followingCount = followingCount
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

    fun withAdditionalProperty(name: String, value: Any): Profile {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = -5387054277563748043L
    }
}