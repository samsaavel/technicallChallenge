package com.greenv.pt_upax_gccv.ux.pokemon.list

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenv.pt_upax_gccv.databinding.ItemRowBinding
import com.greenv.pt_upax_gccv.network.models.PokemonModel

interface PokemonSelectedListener {
    fun onPokemonSelected(id: Int)
}

class ListViewHolder(
    private val binding: ItemRowBinding,
    private val pokemonSelected: PokemonSelectedListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindModelToView(pokemonModel: PokemonModel) {
        val cardView: CardView = binding.cardPokemon
        cardView.setOnClickListener {
            cardView.isSelected = !cardView.isSelected
        }
        binding.pokemonName.text = pokemonModel.name
        binding.root.setOnClickListener {
            pokemonModel.id?.let { pokemonSelected.onPokemonSelected(it) }
        }
    }

    fun bindSpriteToView(spriteUrl: String) {
        Glide.with(binding.spriteImage.context)
            .load(spriteUrl)
            .into(binding.spriteImage)
    }
}