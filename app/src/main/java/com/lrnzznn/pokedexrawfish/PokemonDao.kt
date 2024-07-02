package com.lrnzznn.pokedexrawfish

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Dao
interface PokemonDao{
    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemon(): Flow<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: Pokemon)

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)

    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAllPokemon()

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemonPagingSource(): PagingSource<Int, Pokemon>
}