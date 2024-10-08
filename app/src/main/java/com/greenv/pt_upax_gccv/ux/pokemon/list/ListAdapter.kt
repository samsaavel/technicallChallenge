package com.greenv.pt_upax_gccv.ux.pokemon.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenv.pt_upax_gccv.databinding.ItemRowBinding
import com.greenv.pt_upax_gccv.network.models.PokemonModel

class ListAdapter(
    private var pokemonList: ArrayList<PokemonModel>,
    private val pokemonSelected: PokemonSelectedListener,
) : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, pokemonSelected)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val pokemonModel = pokemonList[position]
        holder.bindModelToView(pokemonModel)

        val spriteUrl = pokemonModel.spriteUrl
        if (spriteUrl != null) {
            holder.bindSpriteToView(spriteUrl)
        }

    }

    fun addData(newPokemonList: List<PokemonModel>) {
        val filteredList = newPokemonList.filter { newPokemon ->
            pokemonList.none() { it.name == newPokemon.name }
        }
        val startPosition = pokemonList.size
        pokemonList.addAll(filteredList)
        notifyItemRangeInserted(startPosition, newPokemonList.size)
    }
}