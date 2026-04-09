package edu.cs134.joketeller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class JokeResponse(
    val setup: String,
    val punchline: String
)

interface JokeApi {
    @GET("jokes/random")
    suspend fun getRandomJoke(): JokeResponse
}

// Helper singleton to manage network and formatting
object JokeRepository {
    private val api: JokeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(JokeApi::class.java)
    }

    // Suspend function that handles fetching + formatting + errors
    suspend fun fetchJokeText(): String = withContext(Dispatchers.IO) {
        try {
            val joke = api.getRandomJoke()
            "${joke.setup} ... ${joke.punchline}"
        } catch (e: Exception) {
            "Error fetching joke: ${e.message}"
        }
    }
}