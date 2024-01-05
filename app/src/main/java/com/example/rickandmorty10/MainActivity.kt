package com.example.rickandmorty10

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {

    private lateinit var adapter: RickRecycler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RickRecycler()

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter
        RetrofitInstance.api.getDetails().enqueue(object :Callback<RickMorty>{
            override fun onResponse(call: Call<RickMorty>, response: Response<RickMorty>) {
                if (response.body()!=null){
                    adapter.setData(response.body()!!.results)
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<RickMorty>, t: Throwable) {
                Log.d("TAG",t.message.toString())
            }

        })
    }
}