package com.example.proyecto1_almacolina

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryAPI {
    @GET("en/{word}")
    suspend fun getMeaning(@Path("word") word : String) : Response<List<WordResult>>
}