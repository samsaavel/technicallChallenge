package com.greenv.pt_upax_gccv.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pokemon_table",
    indices = [Index(value = ["pokedexId", "name"], unique = true)]
)
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "pokedexId") val pokedexId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "front_default") val frontDefault: String?,
    @ColumnInfo(name = "back_default") val backDefault: String?,
    @ColumnInfo(name = "front_shiny") val frontShiny: String?,
    @ColumnInfo(name = "back_shiny") val backShiny: String?,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean,
)