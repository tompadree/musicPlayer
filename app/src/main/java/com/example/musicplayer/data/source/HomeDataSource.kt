package com.example.musicplayer.data.source

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.models.SessionObject
import com.example.musicplayer.data.models.Result

/**
 * @author Tomislav Curis
 */
interface HomeDataSource {

    fun observeSessions(searchQuery: String): LiveData<Result<List<SessionObject>>>

    suspend fun saveSessions(sessions: List<SessionObject>)

    suspend fun getSessions(searchQuery: String, offset: Int, limit: Int): Result<List<SessionObject>>

    suspend fun deleteSessions()
}