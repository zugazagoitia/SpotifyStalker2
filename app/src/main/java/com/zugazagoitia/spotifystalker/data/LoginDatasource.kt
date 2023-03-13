package com.zugazagoitia.spotifystalker.data

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.MessageFormat
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.ByteString
import com.spotify.Authentication
import com.spotify.Authentication.LoginCredentials
import com.spotify.connectstate.Connect
import com.zugazagoitia.spotifystalker.SharedPropertiesConstants
import com.zugazagoitia.spotifystalker.model.LoggedInUser
import com.zugazagoitia.spotifystalker.model.RichProfile
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import xyz.gianlu.librespot.common.Utils
import xyz.gianlu.librespot.core.Session
import java.io.IOException
import java.security.GeneralSecurityException


class LoginDatasource (private val context: Context) {

    fun login(username: String, password: String, rememberPassword: Boolean): Result<LoggedInUser> {
        val conf: Session.Configuration = Session.Configuration.Builder()
            .setStoreCredentials(false)
            .setCacheEnabled(false)
            .build()

        try {
            Session.Builder(conf)
                .userPass(username, password)
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .create().use { spotifySession ->
                    val token: String = spotifySession.tokens().get("user-read-currently-playing")
                    val user = getProfile(token, username)?.let {
                        LoggedInUser(token, username,
                            it
                        )
                    }
                    if (rememberPassword) {
                        try {
                            saveSessionToDevice(spotifySession, username)
                        } catch (e: GeneralSecurityException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    when {
                        user != null -> return Result.success(user)
                        else -> return Result.failure(IOException("Error logging in"))
                    }
                }
        } catch (e: Exception) {
            return Result.failure(IOException("Error logging in", e))
        }
    }

    fun loginBlob(username: String, blob: String, deviceId: String): Result<LoggedInUser> {
        val conf = Session.Configuration.Builder()
            .setStoreCredentials(false)
            .setCacheEnabled(false)
            .build()
        val credentials = LoginCredentials.newBuilder()
            .setUsername(username)
            .setAuthData(ByteString.copyFrom(Utils.fromBase64(blob)))
            .setTyp(Authentication.AuthenticationType.AUTHENTICATION_STORED_SPOTIFY_CREDENTIALS)
            .build()
        try {
            Session.Builder(conf)
                .setDeviceId(deviceId)
                .credentials(credentials)
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .create().use { spotifySession ->
                    val token =
                        spotifySession.tokens()["user-read-currently-playing"]
                    val user = LoggedInUser(
                        token, spotifySession.username(),
                        getProfile(token, spotifySession.username())!!
                    )
                    return Result.success(user)
                }
        } catch (e: java.lang.Exception) {
            return Result.failure(IOException("Error logging in", e))
        }
    }

    fun logout() {
        sharedPreferences().edit().clear().apply()
    }
    //TODO: Add biometric login? https://developer.android.com/training/sign-in/biometric-auth
    fun loginWithSavedCredentials(): Result<LoggedInUser>{
        return try {
            val sharedPreferences = sharedPreferences()
            when {
                sharedPreferences.contains(SharedPropertiesConstants.SP_USERNAME_KEY)
                        && sharedPreferences.contains(SharedPropertiesConstants.SP_CREDENTIALS_KEY)
                        && sharedPreferences.contains(SharedPropertiesConstants.SP_DEVICE_ID) -> {
                    loginBlob(
                        sharedPreferences.getString(SharedPropertiesConstants.SP_USERNAME_KEY, "")!!,
                        sharedPreferences.getString(SharedPropertiesConstants.SP_CREDENTIALS_KEY, "")!!,
                        sharedPreferences.getString(SharedPropertiesConstants.SP_DEVICE_ID, "")!!
                    )
                }
                else -> {
                    Result.failure(IOException("Error logging in"))
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(IOException("Error logging in", e))
        }
    }

    //TODO: Add biometric login? https://developer.android.com/training/sign-in/biometric-auth
    private fun saveSessionToDevice(
        spotifySession: Session,
        username: String,
    ) {
        val sharedPreferences: SharedPreferences =
            sharedPreferences()
        println("Valid: " + spotifySession.isValid)
        val credentials: String = Utils.toBase64(
            spotifySession.apWelcome().reusableAuthCredentials
                .toByteArray()
        )
        val deviceId: String = spotifySession.deviceId()
        sharedPreferences.edit().clear()
            .putString(SharedPropertiesConstants.SP_USERNAME_KEY, username)
            .putString(
                SharedPropertiesConstants.SP_CREDENTIALS_KEY,
                credentials
            )
            .putString(SharedPropertiesConstants.SP_DEVICE_ID, deviceId)
            .apply()
    }

    private fun sharedPreferences(): SharedPreferences {
        val spec = KeyGenParameterSpec.Builder(
            SharedPropertiesConstants.MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()
        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            SharedPropertiesConstants.SP_FILE_NAME,
            masterKey,  // masterKey created above
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getProfile(token: String, username: String): RichProfile? {
        val url: String = MessageFormat.format(
            "https://spclient.wg.spotify.com/user-profile-view/v3/profile/{0}",
            username
        )
        val client = OkHttpClient()

        //Call api using okhttp
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()
        return try {
            val response: Response = client.newCall(request).execute()
            val responseBody: String = response.body?.string() ?: ""
            val objMapper = ObjectMapper()
            objMapper.readValue(responseBody, RichProfile::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}