package com.example.rickandmorty10

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var characterImage: ImageView
    private lateinit var characterName: TextView
    private lateinit var characterStatus: TextView
    private lateinit var characterLastLocation: TextView
    private lateinit var characterFirstSeen: TextView
    private lateinit var characterSpecies: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        // Get references to views (assuming IDs are in layout file)
        characterImage = findViewById(R.id.character_image)
        characterName = findViewById(R.id.character_name)
        characterStatus = findViewById(R.id.character_description)
        characterLastLocation = findViewById(R.id.character_location)
        characterFirstSeen = findViewById(R.id.character_seen)
        characterSpecies = findViewById(R.id.character_species)

        // Retrieve character data from intent (assuming Result is Parcelable)
        val character = intent.getParcelableExtra<Result>("character")

        // Display character information
        if (character != null) {
            Glide.with(this).load(character.image).into(characterImage)
            characterName.text = character.name
            characterStatus.text = "Status:\n ${character.status}"
            characterLastLocation.text = "Last known location:\n ${character.location.name}"
            characterFirstSeen.text = "First seen in:\n ${character.origin.name}"
            characterSpecies.text = "Species:\n ${character.species}"
        } else {
            println(" lol ")
        }
    }
}