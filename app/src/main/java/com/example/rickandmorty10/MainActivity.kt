package com.example.rickandmorty10

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RickRecycler()

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter

        val charactersList = mutableListOf<Result>() // Store all characters
        val allCharacters = mutableListOf<Result>()

        for (page in 1..43) {
            RetrofitInstance.api.getDetails(page).enqueue(object : Callback<RickMorty> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<RickMorty>, response: Response<RickMorty>) {
                    if (response.isSuccessful && response.body() != null) {
                        allCharacters.addAll(response.body()!!.results)
                    }

                    if (page == 43) { // All pages loaded
                        charactersList.addAll(allCharacters.sortedBy { it.created }) // Sort by name (adjust as needed)
                        adapter.setData(charactersList)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<RickMorty>, t: Throwable) {
                    // Handle network errors
                }
            })
        }
    }
}