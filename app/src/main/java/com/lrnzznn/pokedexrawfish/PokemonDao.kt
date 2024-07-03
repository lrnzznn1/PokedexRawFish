package com.lrnzznn.pokedexrawfish

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM pokemon_table LIMIT :limit OFFSET :offset")
    suspend fun getPokemonInRange(offset: Int, limit: Int): List<Pokemon>
}