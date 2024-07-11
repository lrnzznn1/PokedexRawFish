package com.lrnzznn.pokedexrawfish.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lrnzznn.pokedexrawfish.utility.Converters


@Database(entities = [Pokemon::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        // Singleton pattern to get or create the database instance
        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                )
                    .fallbackToDestructiveMigration() // Allows Room to recreate tables if migrations are not possible
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
