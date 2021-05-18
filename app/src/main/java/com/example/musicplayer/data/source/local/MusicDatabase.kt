package com.example.musicplayer.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicplayer.data.models.*

/**
 * @author Tomislav Curis
 */
@Database(entities = [SessionObject::class], version = 1, exportSchema = false)
@TypeConverters(MainDataObjectConverter::class, CurrentTrackObjectConverter::class, SessionsConverter::class, GenresConverter::class)
abstract class MusicDatabase: RoomDatabase() {
    abstract fun getMusicDao(): MusicDAO
}