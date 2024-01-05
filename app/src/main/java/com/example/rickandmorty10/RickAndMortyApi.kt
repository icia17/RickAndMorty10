package com.example.rickandmorty10

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interface that defines the
// endpoints of the Rick and Morty API
interface RickApi  {
    // HTTP GET request to the character endpoint
    // https://rickandmortyapi.com/api/character
    @GET("character")
    fun getDetails() : Call<RickMorty>
}

// Object that holds a single
// instance of the RickApi interface,
// initialized using Retrofit
object RetrofitInstance {
    // Property that lazily initializes
    // the RickApi implementation
    val api : RickApi by lazy {
        // Build the Retrofit instance with
        // a base URL and a Gson converter factory
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickApi::class.java)
    }
}