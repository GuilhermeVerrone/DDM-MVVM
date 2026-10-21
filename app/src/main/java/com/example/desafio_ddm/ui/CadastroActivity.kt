package com.example.desafio_ddm.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import coil.load
import com.example.desafio_ddm.R
import com.example.desafio_ddm.databinding.ActivityCadastroBinding
import com.example.desafio_ddm.viewmodel.MovieViewModel
import com.example.desafio_ddm.viewmodel.SaveResult

/**
 * Formulário usado para cadastrar um filme novo ou editar um já salvo.
 *
 * Recebe [EXTRA_MOVIE_ID] com o id do filme a editar; quando o extra vem como
 * [MovieViewModel.NO_ID], a tela entra em modo de cadastro.
 */
class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val viewModel: MovieViewModel by viewModels()

    private var movieId: Long = MovieViewModel.NO_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getLongExtra(EXTRA_MOVIE_ID, MovieViewModel.NO_ID)
        if (movieId != MovieViewModel.NO_ID) {
            fillFormForEdit()
        }

        // Pré-visualização do poster com Coil enquanto a URL é digitada.
        binding.etPosterUrl.doAfterTextChanged { text ->
            loadPreview(text?.toString()?.trim().orEmpty())
        }

        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnSalvar.setOnClickListener { save() }
    }

    private fun fillFormForEdit() {
        val movie = viewModel.findById(movieId)
        if (movie == null) {
            // O filme pode ter sido excluído em outra tela.
            Toast.makeText(this, R.string.filme_nao_encontrado, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvTitle.setText(R.string.title_editar_filme)
        binding.btnSalvar.setText(R.string.btn_atualizar)
        binding.etTitulo.setText(movie.title)
        binding.etGenero.setText(movie.genre)
        binding.etNota.setText(movie.rating.toString())
        binding.etPosterUrl.setText(movie.posterUrl)
    }

    private fun loadPreview(url: String) {
        binding.ivPreview.load(url) {
            crossfade(true)
            placeholder(R.drawable.ic_movie_placeholder)
            error(R.drawable.ic_movie_placeholder)
        }
    }

    private fun save() {
        val result = viewModel.save(
            id = movieId,
            title = binding.etTitulo.text.toString(),
            genre = binding.etGenero.text.toString(),
            ratingInput = binding.etNota.text.toString(),
            posterUrl = binding.etPosterUrl.text.toString()
        )

        when (result) {
            SaveResult.CREATED -> {
                Toast.makeText(this, R.string.filme_salvo, Toast.LENGTH_SHORT).show()
                clearForm()
            }

            SaveResult.UPDATED -> {
                Toast.makeText(this, R.string.filme_atualizado, Toast.LENGTH_SHORT).show()
                finish()
            }

            SaveResult.EMPTY_FIELDS ->
                Toast.makeText(this, R.string.erro_campos_obrigatorios, Toast.LENGTH_SHORT).show()

            SaveResult.INVALID_RATING ->
                Toast.makeText(this, R.string.erro_nota_invalida, Toast.LENGTH_SHORT).show()

            SaveResult.NOT_FOUND -> {
                Toast.makeText(this, R.string.filme_nao_encontrado, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun clearForm() {
        binding.etTitulo.text.clear()
        binding.etGenero.text.clear()
        binding.etNota.text.clear()
        binding.etPosterUrl.text.clear()
        binding.etTitulo.requestFocus()
    }

    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
    }
}
