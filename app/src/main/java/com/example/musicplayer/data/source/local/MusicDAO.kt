package com.example.musicplayer.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.models.SessionObject

/**
 * @author Tomislav Curis
 */

@Dao
interface MusicDAO {


    /**
     * Observes list of sessions.
     *
     * @return all sessions.
     */
    @Query("SELECT * FROM sessions WHERE currTrack_title LIKE :searchQuery ORDER BY currTrack_title ASC LIMIT :limit OFFSET :offset")
    fun observeSessions(searchQuery: String, offset: Int, limit: Int): LiveData<List<SessionObject>>

    /**
     * Delete all sessions.
     */
    @Query("DELETE FROM sessions")
    suspend fun deleteSessions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSessions(sessions: List<SessionObject>) // : LongArray

    @Query("SELECT * FROM sessions WHERE currTrack_title LIKE :searchQuery ORDER BY currTrack_title ASC LIMIT :limit OFFSET :offset")
    fun getSessions(searchQuery: String, offset: Int, limit: Int): List<SessionObject>


}