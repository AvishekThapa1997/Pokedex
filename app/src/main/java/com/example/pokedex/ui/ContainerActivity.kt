package com.example.pokedex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityContainerBinding
import com.example.pokedex.viewmodel.PokemonDetailsViewModel

class ContainerActivity : AppCompatActivity() {
    private val containerBinding: ActivityContainerBinding by lazy {
        ActivityContainerBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(containerBinding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
    }
}