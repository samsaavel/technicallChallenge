package com.greenv.pt_upax_gccv.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.greenv.pt_upax_gccv.data.dao.PokemonDao
import com.greenv.pt_upax_gccv.data.entities.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class PokemonDB : RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDB? = null

        fun getDatabase(context: Context): PokemonDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDB::class.java,
                    "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}