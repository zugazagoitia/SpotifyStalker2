package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "image_url", "followers_count", "is_following")
class RecentlyPlayedArtist {
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
    var followersCount: Int? = null

    @get:JsonProperty("is_following")
    @set:JsonProperty("is_following")
    @JsonProperty("is_following")
    var isFollowing: Boolean? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param isFollowing
     * @param imageUrl
     * @param name
     * @param followersCount
     * @param uri
     */
    constructor(
        uri: String?,
        name: String?,
        imageUrl: String?,
        followersCount: Int?,
        isFollowing: Boolean?
    ) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
        this.followersCount = followersCount
        this.isFollowing = isFollowing
    }

    fun withUri(uri: String?): RecentlyPlayedArtist {
        this.uri = uri
        return this
    }

    fun withName(name: String?): RecentlyPlayedArtist {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): RecentlyPlayedArtist {
        this.imageUrl = imageUrl
        return this
    }

    fun withFollowersCount(followersCount: Int?): RecentlyPlayedArtist {
        this.followersCount = followersCount
        return this
    }

    fun withIsFollowing(isFollowing: Boolean?): RecentlyPlayedArtist {
        this.isFollowing = isFollowing
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

    fun withAdditionalProperty(name: String, value: Any): RecentlyPlayedArtist {
        additionalProperties[name] = value
        return this
    }
}