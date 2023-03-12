package com.zugazagoitia.spotifystalker.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class LoggedInUser(val token: String, val username: String, val user: RichProfile)