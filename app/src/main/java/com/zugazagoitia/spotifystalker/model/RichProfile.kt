package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "uri",
    "name",
    "image_url",
    "followers_count",
    "following_count",
    "is_following",
    "recently_played_artists",
    "public_playlists",
    "total_public_playlists_count",
    "has_spotify_image",
    "color"
)
class RichProfile {
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

    @get:JsonProperty("following_count")
    @set:JsonProperty("following_count")
    @JsonProperty("following_count")
    var followingCount: Int? = null

    @get:JsonProperty("is_following")
    @set:JsonProperty("is_following")
    @JsonProperty("is_following")
    var isFollowing: Boolean? = null

    @get:JsonProperty("recently_played_artists")
    @set:JsonProperty("recently_played_artists")
    @JsonProperty("recently_played_artists")
    var recentlyPlayedArtists: List<RecentlyPlayedArtist>? = null

    @get:JsonProperty("public_playlists")
    @set:JsonProperty("public_playlists")
    @JsonProperty("public_playlists")
    var publicPlaylists: List<PublicPlaylist>? = null

    @get:JsonProperty("total_public_playlists_count")
    @set:JsonProperty("total_public_playlists_count")
    @JsonProperty("total_public_playlists_count")
    var totalPublicPlaylistsCount: Int? = null

    @get:JsonProperty("has_spotify_image")
    @set:JsonProperty("has_spotify_image")
    @JsonProperty("has_spotify_image")
    var hasSpotifyImage: Boolean? = null

    @get:JsonProperty("color")
    @set:JsonProperty("color")
    @JsonProperty("color")
    var color: Int? = null

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
     * @param color
     * @param imageUrl
     * @param name
     * @param hasSpotifyImage
     * @param followersCount
     * @param uri
     * @param followingCount
     * @param recentlyPlayedArtists
     * @param publicPlaylists
     * @param totalPublicPlaylistsCount
     */
    constructor(
        uri: String?,
        name: String?,
        imageUrl: String?,
        followersCount: Int?,
        followingCount: Int?,
        isFollowing: Boolean?,
        recentlyPlayedArtists: List<RecentlyPlayedArtist>?,
        publicPlaylists: List<PublicPlaylist>?,
        totalPublicPlaylistsCount: Int?,
        hasSpotifyImage: Boolean?,
        color: Int?
    ) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
        this.followersCount = followersCount
        this.followingCount = followingCount
        this.isFollowing = isFollowing
        this.recentlyPlayedArtists = recentlyPlayedArtists
        this.publicPlaylists = publicPlaylists
        this.totalPublicPlaylistsCount = totalPublicPlaylistsCount
        this.hasSpotifyImage = hasSpotifyImage
        this.color = color
    }

    fun withUri(uri: String?): RichProfile {
        this.uri = uri
        return this
    }

    fun withName(name: String?): RichProfile {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): RichProfile {
        this.imageUrl = imageUrl
        return this
    }

    fun withFollowersCount(followersCount: Int?): RichProfile {
        this.followersCount = followersCount
        return this
    }

    fun withFollowingCount(followingCount: Int?): RichProfile {
        this.followingCount = followingCount
        return this
    }

    fun withIsFollowing(isFollowing: Boolean?): RichProfile {
        this.isFollowing = isFollowing
        return this
    }

    fun withRecentlyPlayedArtists(recentlyPlayedArtists: List<RecentlyPlayedArtist>?): RichProfile {
        this.recentlyPlayedArtists = recentlyPlayedArtists
        return this
    }

    fun withPublicPlaylists(publicPlaylists: List<PublicPlaylist>?): RichProfile {
        this.publicPlaylists = publicPlaylists
        return this
    }

    fun withTotalPublicPlaylistsCount(totalPublicPlaylistsCount: Int?): RichProfile {
        this.totalPublicPlaylistsCount = totalPublicPlaylistsCount
        return this
    }

    fun withHasSpotifyImage(hasSpotifyImage: Boolean?): RichProfile {
        this.hasSpotifyImage = hasSpotifyImage
        return this
    }

    fun withColor(color: Int?): RichProfile {
        this.color = color
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

    fun withAdditionalProperty(name: String, value: Any): RichProfile {
        additionalProperties[name] = value
        return this
    }
}