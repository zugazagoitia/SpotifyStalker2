package com.zugazagoitia.spotifystalker.data

import com.zugazagoitia.spotifystalker.model.RichProfile
import com.zugazagoitia.spotifystalker.model.UserPlayingInfo


interface IFriendListRepository {
    fun updateFriendList(): List<UserPlayingInfo>
}

class FriendListRepository
    private constructor(private val dataSource: FriendListDatasource) : IFriendListRepository {

    var state: MutableList<UserPlayingInfo> = mutableListOf()

    override fun updateFriendList(): List<UserPlayingInfo> {
        state = dataSource.getFriendList()
        return state.toList()
    }

    companion object {
        @Volatile
        private var instance: FriendListRepository? = null
        fun getInstance(dataSource: FriendListDatasource): FriendListRepository? {
            if (instance == null) {
                instance = FriendListRepository(dataSource)
            }
            return instance
        }
    }
}