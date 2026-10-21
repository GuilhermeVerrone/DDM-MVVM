package com.example.desafio_ddm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio_ddm.R
import com.example.desafio_ddm.databinding.ActivityListaBinding
import com.example.desafio_ddm.model.Movie
import com.example.desafio_ddm.viewmodel.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Lista de filmes favoritos: observa a ViewModel e abre o formulário de
 * cadastro/edição. Nenhuma alteração de dados é feita direto na lista.
 */
class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding
    private val viewModel: MovieViewModel by viewModels()

    private val adapter = MovieAdapter(
        onEditClick = { movie -> openForm(movie.id) },
        onDeleteClick = { movie -> confirmDelete(movie) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter

        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnNovoFilme.setOnClickListener { openForm(MovieViewModel.NO_ID) }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this) { movies ->
            adapter.submitList(movies)
        }

        viewModel.isEmpty.observe(this) { isEmpty ->
            binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvMovies.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    private fun openForm(movieId: Long) {
        val intent = Intent(this, CadastroActivity::class.java)
            .putExtra(CadastroActivity.EXTRA_MOVIE_ID, movieId)
        startActivity(intent)
    }

    private fun confirmDelete(movie: Movie) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.titulo_confirmar_exclusao)
            .setMessage(getString(R.string.msg_confirmar_exclusao, movie.title))
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.btn_excluir) { _, _ ->
                viewModel.delete(movie)
                Snackbar.make(binding.root, R.string.filme_excluido, Snackbar.LENGTH_SHORT).show()
            }
            .show()
    }
}
