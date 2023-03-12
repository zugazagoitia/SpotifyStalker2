package com.zugazagoitia.spotifystalker.data

import android.icu.text.MessageFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.zugazagoitia.spotifystalker.model.FriendList
import com.zugazagoitia.spotifystalker.model.LoggedInUser
import com.zugazagoitia.spotifystalker.model.UserPlayingInfo
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors


class FriendListDatasource (private val user: LoggedInUser){

    fun getFriendList(): MutableList<UserPlayingInfo> {
        val username: String = user.username
        val token: String = user.token
        val url: String = MessageFormat.format(
            "https://spclient.wg.spotify.com/user-profile-view/v3/profile/{0}/following/",
            username
        )
        val client = OkHttpClient()

        //Get the list of friends from the spotify api
        val friendsRequest: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()
        val friendList: MutableList<UserPlayingInfo> = mutableListOf()
        try {
            client.newCall(friendsRequest).execute().use { response ->
                val jsonMapper = ObjectMapper()
                val responseBody = response.body!!.string()
                val friends: FriendList = jsonMapper.readValue(responseBody, FriendList::class.java)
                friends.profiles = (friends.profiles
                    .stream()
                    .filter { profile ->
                        profile.uri!!.contains("user")
                    } //Filter out artists
                    .collect(Collectors.toList()))
                val activityUrl =
                    "https://spclient.wg.spotify.com/presence-view/v1/user/{0}"
                val countDownLatch = CountDownLatch(friends.profiles.size)
                for (p in friends.profiles) {
                    val userActivityRequest: Request = Request.Builder()
                        .url(MessageFormat.format(activityUrl, p.uri!!.split(":")[2]))
                        .addHeader("Authorization", "Bearer $token")
                        .build()

                    //Asynchronous call
                    client.newCall(userActivityRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            countDownLatch.countDown()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val body: String = response.body!!.string()
                            try {
                                friendList.add(
                                    jsonMapper.readValue(
                                        body,
                                        UserPlayingInfo::class.java
                                    )
                                )
                            } catch (e: Exception) {
                                // This happens when the user is not playing anything
                            }
                            countDownLatch.countDown()
                        }
                    })
                }
                countDownLatch.await()
                friendList.sortWith { o1: UserPlayingInfo, o2: UserPlayingInfo -> (o2.timestamp - o1.timestamp).toInt() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return friendList
    }

}