package com.greenv.pt_upax_gccv.ux.pokemon.details

import androidx.recyclerview.widget.RecyclerView
import com.greenv.pt_upax_gccv.databinding.TypeItemBinding
import com.greenv.pt_upax_gccv.network.models.PokemonModel

class TypeViewHolder(private val binding: TypeItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindModeltoView(type: PokemonModel) {
        binding.buttonType.text = type.name
    }

}