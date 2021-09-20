package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.HeaderLayoutBinding

class HeaderAdapter(private val query: (String) -> Unit) :
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    class HeaderViewHolder(
        private val headerLayoutBinding: HeaderLayoutBinding,
    ) : RecyclerView.ViewHolder(headerLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val headerLayoutBinding = HeaderLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        headerLayoutBinding.etSearchText.addTextChangedListener { text ->
            query(text.toString())
        }
        return HeaderViewHolder(headerLayoutBinding)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {

    }

    override fun getItemCount() = 1
}