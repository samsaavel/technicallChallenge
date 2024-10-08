package com.greenv.pt_upax_gccv.ux.pokemon.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenv.pt_upax_gccv.databinding.TypeItemBinding
import com.greenv.pt_upax_gccv.network.models.PokemonModel

class TypeAdapter(private var types: List<PokemonModel>) : RecyclerView.Adapter<TypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = TypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun getItemCount(): Int = types.size

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = types[position]
        holder.bindModeltoView(type)
    }

}