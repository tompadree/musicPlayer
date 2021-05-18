package com.example.musicplayer.data.source

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.models.Result
import com.example.musicplayer.data.models.SessionObject
import java.lang.Exception

/**
 * @author Tomislav Curis
 */
class HomeRepositoryImpl(
        private val localDataSource: HomeDataSource,
        private val remoteDataSource: HomeDataSource
) : HomeRepository {

    override fun observeSessions(searchQuery: String): LiveData<Result<List<SessionObject>>> =
            localDataSource.observeSessions(searchQuery)

    override suspend fun saveSessions(sessions: List<SessionObject>) =
            localDataSource.saveSessions(sessions)

    override suspend fun getSessions(update: Boolean, searchQuery: String, offset: Int, limit: Int): Result<List<SessionObject>> {
        if(update)
            try {
                updateSessionsFromRemote(searchQuery, offset, limit)
            } catch (e: Exception) {
                return Result.Error(e)
            }
        return localDataSource.getSessions(searchQuery, offset, limit)
    }

    private suspend fun updateSessionsFromRemote(searchQuery: String, offset: Int, limit: Int) {
            val remoteSessions = remoteDataSource.getSessions(searchQuery, offset, limit)
            if(remoteSessions is Result.Success) {
                localDataSource.saveSessions(remoteSessions.data)
            } else if (remoteSessions is Result.Error) {
                throw remoteSessions.exception
            }

    }
}