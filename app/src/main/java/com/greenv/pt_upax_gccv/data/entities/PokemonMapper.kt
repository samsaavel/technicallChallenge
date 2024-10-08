package com.greenv.pt_upax_gccv.data.entities

import com.greenv.pt_upax_gccv.network.models.DetailsModel
import com.greenv.pt_upax_gccv.network.models.PokemonModel
import com.greenv.pt_upax_gccv.network.models.Sprite
import com.greenv.pt_upax_gccv.network.models.Type

object PokemonMapper {

    fun entityToPokemonModel(entity: PokemonEntity): PokemonModel {
        return PokemonModel(
            name = entity.name,
            url = entity.url,
            spriteUrl = entity.frontDefault
        )
    }

    fun entityToDetailsModel(entity: PokemonEntity): DetailsModel {
        val sprite = Sprite(
            front_default = entity.frontDefault,
            back_default = entity.backDefault,
            front_female = null,
            back_female = null,
            front_shiny = entity.frontShiny,
            back_shiny = entity.backShiny
        )

        // Convert comma-separated type string from entity to List<Type>
        val types = entity.type.split(",").map { typeName ->
            Type(
                slot = 1,
                type = PokemonModel(
                    name = typeName,
                    url = "https://pokeapi.co/api/v2/type/$typeName/"
                )
            )
        }.toCollection(ArrayList())

        return DetailsModel(
            height = entity.height,
            sprites = sprite,
            types = types,
            weight = entity.weight,
            id = entity.pokedexId,
            name = entity.name
        )
    }

    fun modelToEntity(
        model: PokemonModel,
        details: DetailsModel,
    ): PokemonEntity {
        return PokemonEntity(
            pokedexId = model.id ?: 0,
            name = model.name ?: "",
            url = model.url ?: "",
            height = details.height,
            weight = details.weight,
            type = details.types.joinToString(",") { it.type.name ?: "Unknown" },
            frontDefault = details.sprites.front_default ?: "",
            backDefault = details.sprites.back_default ?: "",
            frontShiny = details.sprites.front_shiny ?: "",
            backShiny = details.sprites.back_shiny ?: "",
            isFavorite = false,
        )
    }
}