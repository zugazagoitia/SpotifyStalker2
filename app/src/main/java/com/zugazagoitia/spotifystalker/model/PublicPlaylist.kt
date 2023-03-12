package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "image_url", "followers_count", "owner_name", "owner_uri")
class PublicPlaylist {
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

    @get:JsonProperty("owner_name")
    @set:JsonProperty("owner_name")
    @JsonProperty("owner_name")
    var ownerName: String? = null

    @get:JsonProperty("owner_uri")
    @set:JsonProperty("owner_uri")
    @JsonProperty("owner_uri")
    var ownerUri: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param ownerName
     * @param imageUrl
     * @param name
     * @param ownerUri
     * @param followersCount
     * @param uri
     */
    constructor(
        uri: String?,
        name: String?,
        imageUrl: String?,
        followersCount: Int?,
        ownerName: String?,
        ownerUri: String?
    ) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
        this.followersCount = followersCount
        this.ownerName = ownerName
        this.ownerUri = ownerUri
    }

    fun withUri(uri: String?): PublicPlaylist {
        this.uri = uri
        return this
    }

    fun withName(name: String?): PublicPlaylist {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): PublicPlaylist {
        this.imageUrl = imageUrl
        return this
    }

    fun withFollowersCount(followersCount: Int?): PublicPlaylist {
        this.followersCount = followersCount
        return this
    }

    fun withOwnerName(ownerName: String?): PublicPlaylist {
        this.ownerName = ownerName
        return this
    }

    fun withOwnerUri(ownerUri: String?): PublicPlaylist {
        this.ownerUri = ownerUri
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

    fun withAdditionalProperty(name: String, value: Any): PublicPlaylist {
        additionalProperties[name] = value
        return this
    }
}