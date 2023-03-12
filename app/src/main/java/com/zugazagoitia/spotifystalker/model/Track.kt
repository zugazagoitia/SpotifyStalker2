package com.zugazagoitia.spotifystalker.model

import com.fasterxml.jackson.annotation.*
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("uri", "name", "imageUrl", "album", "artist", "context")
class Track : Serializable {
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

    @get:JsonProperty("album")
    @set:JsonProperty("album")
    @JsonProperty("album")
    var album: Album? = null

    @get:JsonProperty("artist")
    @set:JsonProperty("artist")
    @JsonProperty("artist")
    var artist: Artist? = null

    @get:JsonProperty("context")
    @set:JsonProperty("context")
    @JsonProperty("context")
    var context: Context? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param artist
     * @param album
     * @param imageUrl
     * @param name
     * @param context
     * @param uri
     */
    constructor(
        uri: String?,
        name: String?,
        imageUrl: String?,
        album: Album?,
        artist: Artist?,
        context: Context?
    ) : super() {
        this.uri = uri
        this.name = name
        this.imageUrl = imageUrl
        this.album = album
        this.artist = artist
        this.context = context
    }

    fun withUri(uri: String?): Track {
        this.uri = uri
        return this
    }

    fun withName(name: String?): Track {
        this.name = name
        return this
    }

    fun withImageUrl(imageUrl: String?): Track {
        this.imageUrl = imageUrl
        return this
    }

    fun withAlbum(album: Album?): Track {
        this.album = album
        return this
    }

    fun withArtist(artist: Artist?): Track {
        this.artist = artist
        return this
    }

    fun withContext(context: Context?): Track {
        this.context = context
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

    fun withAdditionalProperty(name: String, value: Any): Track {
        additionalProperties[name] = value
        return this
    }

    companion object {
        private const val serialVersionUID = -1688473816237846524L
    }
}