package com.example.rickandmorty10

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var mainHandler: Handler
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var hasLoaded = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainHandler = Handler(mainLooper)

        if (hasInternetConnection()) {
            characterListBegin()
        } else {
            setContentView(R.layout.activity_loading_screen)
        }

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                mainHandler.post { // Ensure UI updates on the main thread
                    if (hasInternetConnection()) {
                        characterListBegin()
                    }
                }
            }

            override fun onLost(network: Network) {
                mainHandler.post {
                    if (!hasLoaded) {
                        setContentView(R.layout.activity_loading_screen)
                    }
                }
            }
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun characterListBegin() {
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
                        hasLoaded = true
                    }
                }

                override fun onFailure(call: Call<RickMorty>, t: Throwable) {
                    // Handle network errors
                }
            })
        }
    }
}