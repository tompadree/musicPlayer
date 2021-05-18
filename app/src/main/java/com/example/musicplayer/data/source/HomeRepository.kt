package com.example.musicplayer.data.source

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.models.Result
import com.example.musicplayer.data.models.SessionObject

/**
 * @author Tomislav Curis
 */
interface HomeRepository {

    fun observeSessions(searchQuery: String = ""): LiveData<Result<List<SessionObject>>>

    suspend fun saveSessions(sessions: List<SessionObject>)

    suspend fun getSessions(update: Boolean = false, searchQuery: String, offset: Int, limit: Int): Result<List<SessionObject>>

}