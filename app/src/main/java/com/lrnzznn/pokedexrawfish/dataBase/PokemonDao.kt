package com.lrnzznn.pokedexrawfish.dataBase

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


// Data Access Object (DAO) for interacting with the Pokemon table in Room database.
@Dao
interface PokemonDao{

    // Retrieves all Pokemon from the table as a Flow of List of Pokemon.
    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemon(): Flow<List<Pokemon>>


    // Inserts a single Pokemon into the table.
    // If a Pokemon with the same primary key exists, it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: Pokemon)


    // Updates an existing Pokemon in the table.
    @Update
    suspend fun updatePokemon(pokemon: Pokemon)


    // Deletes all Pokemon from the table.
    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAllPokemon()


    // Retrieves a PagingSource for paginating through the Pokemon table.
    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemonPagingSource(): PagingSource<Int, Pokemon>


    // Retrieves a list of Pokemon within a specified range (offset and limit).
    @Query("SELECT * FROM pokemon_table LIMIT :limit OFFSET :offset")
    suspend fun getPokemonInRange(offset: Int, limit: Int): List<Pokemon>
}