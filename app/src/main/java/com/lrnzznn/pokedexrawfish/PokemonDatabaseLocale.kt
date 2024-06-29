package com.lrnzznn.pokedexrawfish

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
