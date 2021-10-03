package com.example.pokedex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityContainerBinding
import com.example.pokedex.viewmodel.PokemonDetailsViewModel

class ContainerActivity : AppCompatActivity() {
    private lateinit var containerBinding: ActivityContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        containerBinding = DataBindingUtil.setContentView(this, R.layout.activity_container)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
    }
}