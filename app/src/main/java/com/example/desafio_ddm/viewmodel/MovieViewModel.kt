package com.example.desafio_ddm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.desafio_ddm.data.MovieRepository
import com.example.desafio_ddm.model.Movie

/** Resultado da tentativa de salvar o formulário, interpretado pela View. */
enum class SaveResult {
    CREATED,
    UPDATED,
    EMPTY_FIELDS,
    INVALID_RATING,
    NOT_FOUND
}

/**
 * ViewModel (camada VM do MVVM) compartilhada pela lista e pelo formulário.
 *
 * As Activities apenas observam os estados expostos aqui e repassam as ações do
 * usuário — nenhuma regra de negócio ou validação vive na View.
 */
class MovieViewModel : ViewModel() {

    private val repository = MovieRepository

    val movies: LiveData<List<Movie>> = repository.observeMovies()

    val isEmpty: LiveData<Boolean> = movies.map { it.isEmpty() }

    fun findById(id: Long): Movie? = repository.findById(id)

    /**
     * Cria um filme novo (quando [id] é [NO_ID]) ou atualiza o filme existente.
     * A nota chega como texto digitado e é validada aqui.
     */
    fun save(
        id: Long,
        title: String,
        genre: String,
        ratingInput: String,
        posterUrl: String
    ): SaveResult {
        val cleanTitle = title.trim()
        val cleanGenre = genre.trim()
        val cleanPosterUrl = posterUrl.trim()
        val cleanRating = ratingInput.trim().replace(',', '.')

        if (cleanTitle.isEmpty() || cleanGenre.isEmpty() ||
            cleanRating.isEmpty() || cleanPosterUrl.isEmpty()
        ) {
            return SaveResult.EMPTY_FIELDS
        }

        val rating = cleanRating.toFloatOrNull()
        if (rating == null || rating < Movie.MIN_RATING || rating > Movie.MAX_RATING) {
            return SaveResult.INVALID_RATING
        }

        if (id == NO_ID) {
            repository.add(cleanTitle, cleanGenre, rating, cleanPosterUrl)
            return SaveResult.CREATED
        }

        val updated = repository.update(id, cleanTitle, cleanGenre, rating, cleanPosterUrl)
        return if (updated) SaveResult.UPDATED else SaveResult.NOT_FOUND
    }

    fun delete(movie: Movie) {
        repository.remove(movie.id)
    }

    companion object {
        /** Id usado quando o formulário está cadastrando um filme novo. */
        const val NO_ID = -1L
    }
}
