package com.example.musicplayer.data.source.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.models.HomeResponse
import com.example.musicplayer.data.models.Result
import com.example.musicplayer.data.models.SessionObject
import com.example.musicplayer.data.source.HomeDataSource
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

/**
 * @author Tomislav Curis
 */
class RemoteDataSource(private val context: Context): HomeDataSource {

    private val observableSessions = MutableLiveData<Result<List<SessionObject>>>()

    override fun observeSessions(searchQuery: String) = observableSessions

    override suspend fun saveSessions(sessions: List<SessionObject>) {
        TODO("Not yet implemented")
    }

    override suspend fun getSessions(searchQuery: String, offset: Int, limit: Int): Result<List<SessionObject>> {

        val response = readFileFromAssets(offset)

        if (response != null) {
                val result = Result.Success(response)
                return result
        }
        return Result.Error(IOException("Error loading data"))
    }

    override suspend fun deleteSessions() {
        TODO("Not yet implemented")
    }

    private fun readFileFromAssets(pagination: Int): List<SessionObject> {
        return try {
            val inputStream: InputStream =
                    if (pagination == 0) {
                        context.assets.open("mock_response_1.json")
                    } else
                        context.assets.open("mock_response_2.json")

            val mappedString  = inputStream.bufferedReader().readText()
            val mappedResponse = Gson().fromJson(mappedString, HomeResponse::class.java)

            return mappedResponse.data.sessions
            } catch (ex: IOException) {
            ex.printStackTrace()
            emptyList<SessionObject>()
        }
    }
}