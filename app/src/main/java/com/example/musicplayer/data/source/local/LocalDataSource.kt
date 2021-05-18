package com.example.musicplayer.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.musicplayer.data.models.Result
import com.example.musicplayer.data.models.SessionObject
import com.example.musicplayer.data.source.HomeDataSource
import com.example.musicplayer.ui.home.HomeViewModel.Companion.CURRENT_INDEX
import com.example.musicplayer.ui.home.HomeViewModel.Companion.PAGE_LIMIT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Tomislav Curis
 */
class LocalDataSource(
        private val dao: MusicDAO,
        private val dispatchers: CoroutineDispatcher = Dispatchers.IO) : HomeDataSource {

    override fun observeSessions(searchQuery: String): LiveData<Result<List<SessionObject>>> {
        return dao.observeSessions("%$searchQuery%", 0, CURRENT_INDEX + PAGE_LIMIT)
                .map {
                        Result.Success(it)
                }
    }

    override suspend fun saveSessions(sessions: List<SessionObject>) =
            withContext(dispatchers) {
                dao.saveSessions(sessions)
            }

    override suspend fun getSessions(searchQuery: String, offset: Int, limit: Int): Result<List<SessionObject>> =
        withContext(dispatchers) {
            return@withContext try {
                Result.Success(dao.getSessions("%$searchQuery%",  offset, limit))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun deleteSessions()=
            withContext(dispatchers) {
                dao.deleteSessions()
            }
}