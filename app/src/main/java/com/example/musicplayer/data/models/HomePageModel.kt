package com.example.musicplayer.data.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

/**
 * @author Tomislav Curis
 */

data class HomeResponse (

    @SerializedName("data")
    val data: MainDataObject

)

data class MainDataObject(

    @SerializedName("sessions")
    val sessions: List<SessionObject>
)

@Entity(tableName = "sessions")
data class SessionObject(

        @PrimaryKey
        @SerializedName("name")
        val name: String,

        @SerializedName("listener_count")
        val listener_count: Int,

        @SerializedName("genres")
        val genres: List<String>,

        @SerializedName("current_track")
        @Embedded(prefix = "currTrack_")
        val current_track: CurrentTrackObject

)

data class CurrentTrackObject (

        @SerializedName("title")
        val title: String,

        @SerializedName("artwork_url")
        val artwork_url: String
)

class MainDataObjectConverter {

    @TypeConverter
    fun stringToMainDataObject(mainDataObject: String): MainDataObject? =
            Gson().fromJson(mainDataObject, MainDataObject::class.java)

    @TypeConverter
    fun fromMainDataObjectToString(mainDataObject: MainDataObject): String? = Gson().toJson(mainDataObject)

}

class CurrentTrackObjectConverter {

    @TypeConverter
    fun stringToCurrentTrackObject(currentTrackObject: String): CurrentTrackObject? =
            Gson().fromJson(currentTrackObject, CurrentTrackObject::class.java)

    @TypeConverter
    fun fromCurrentTrackObjectToString(currentTrackObject: CurrentTrackObject): String? = Gson().toJson(currentTrackObject)

}

class SessionsConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun stringToSessions(value: String): ArrayList<SessionObject> {
            val listType = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromSessionsToString(list: List<SessionObject>): String =
                Gson().toJson(list )

    }
}

class GenresConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun stringToGenres(value: String): ArrayList<String> {
            val listType = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromGenresToString(list: List<String>): String =
                Gson().toJson(list ?: ArrayList<String>())

    }
}