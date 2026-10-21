package com.example.desafio_ddm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.desafio_ddm.viewmodel.MovieViewModel
import com.example.desafio_ddm.viewmodel.SaveResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

/**
 * Testes da lógica de cadastro/edição que vive na ViewModel — a vantagem
 * prática do MVVM aqui é poder validar essas regras sem subir nenhuma tela.
 */
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = MovieViewModel()

    private fun currentMovies() = viewModel.movies.value.orEmpty()

    @Test
    fun `nao salva com campos em branco`() {
        val result = viewModel.save(MovieViewModel.NO_ID, "  ", "Drama", "8.0", "http://poster")

        assertEquals(SaveResult.EMPTY_FIELDS, result)
    }

    @Test
    fun `nao salva com nota fora da faixa`() {
        val result = viewModel.save(MovieViewModel.NO_ID, "Filme", "Drama", "11", "http://poster")

        assertEquals(SaveResult.INVALID_RATING, result)
    }

    @Test
    fun `nao salva com nota nao numerica`() {
        val result = viewModel.save(MovieViewModel.NO_ID, "Filme", "Drama", "otima", "http://poster")

        assertEquals(SaveResult.INVALID_RATING, result)
    }

    @Test
    fun `cadastra filme novo aceitando nota com virgula`() {
        val before = currentMovies().size

        val result = viewModel.save(MovieViewModel.NO_ID, "Tropa de Elite", "Ação", "8,1", "http://poster")

        assertEquals(SaveResult.CREATED, result)
        assertEquals(before + 1, currentMovies().size)
        assertEquals(8.1f, currentMovies().last().rating, 0.001f)
    }

    @Test
    fun `edita filme existente`() {
        viewModel.save(MovieViewModel.NO_ID, "Título antigo", "Drama", "5.0", "http://antigo")
        val id = currentMovies().last().id

        val result = viewModel.save(id, "Título novo", "Comédia", "9.5", "http://novo")

        assertEquals(SaveResult.UPDATED, result)
        val edited = viewModel.findById(id)
        assertNotNull(edited)
        assertEquals("Título novo", edited?.title)
        assertEquals("Comédia", edited?.genre)
        assertEquals("http://novo", edited?.posterUrl)
        assertEquals(9.5f, edited?.rating ?: 0f, 0.001f)
    }

    @Test
    fun `remove filme da lista`() {
        viewModel.save(MovieViewModel.NO_ID, "Para excluir", "Drama", "7.0", "http://poster")
        val movie = currentMovies().last()
        val before = currentMovies().size

        viewModel.delete(movie)

        assertEquals(before - 1, currentMovies().size)
        assertNull(viewModel.findById(movie.id))
    }

    @Test
    fun `editar filme inexistente retorna not found`() {
        val result = viewModel.save(999_999L, "Filme", "Drama", "7.0", "http://poster")

        assertEquals(SaveResult.NOT_FOUND, result)
    }
}
