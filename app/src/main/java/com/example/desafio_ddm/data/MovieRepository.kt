package com.example.desafio_ddm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.desafio_ddm.model.Movie

/**
 * Fonte única de dados dos filmes (camada de dados do MVVM).
 *
 * A lista é mantida em memória e publicada em um [LiveData], então qualquer
 * ViewModel que observe [observeMovies] é notificado automaticamente quando um
 * filme é adicionado, editado ou removido — inclusive em telas diferentes.
 */
object MovieRepository {

    private val movies = mutableListOf<Movie>()
    private val moviesStream = MutableLiveData<List<Movie>>(emptyList())
    private var nextId = 1L

    init {
        seed()
    }

    fun observeMovies(): LiveData<List<Movie>> = moviesStream

    fun findById(id: Long): Movie? = movies.firstOrNull { it.id == id }

    fun add(title: String, genre: String, rating: Float, posterUrl: String) {
        movies.add(Movie(nextId++, title, genre, rating, posterUrl))
        publish()
    }

    /** Substitui os dados do filme de [id]. Retorna false se ele não existir mais. */
    fun update(id: Long, title: String, genre: String, rating: Float, posterUrl: String): Boolean {
        val index = movies.indexOfFirst { it.id == id }
        if (index == -1) return false
        movies[index] = movies[index].copy(
            title = title,
            genre = genre,
            rating = rating,
            posterUrl = posterUrl
        )
        publish()
        return true
    }

    fun remove(id: Long): Boolean {
        val removed = movies.removeAll { it.id == id }
        if (removed) publish()
        return removed
    }

    /** Alguns favoritos de exemplo para a lista não abrir vazia. */
    private fun seed() {
        add("Cidade de Deus", "Drama", 8.6f, "https://picsum.photos/seed/cidadededeus/300/450")
        add("Interestelar", "Ficção científica", 8.7f, "https://picsum.photos/seed/interstellar/300/450")
        add("O Auto da Compadecida", "Comédia", 8.5f, "https://picsum.photos/seed/compadecida/300/450")
    }

    private fun publish() {
        moviesStream.value = movies.toList()
    }
}
