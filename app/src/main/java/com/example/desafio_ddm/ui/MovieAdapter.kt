package com.example.desafio_ddm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.desafio_ddm.R
import com.example.desafio_ddm.databinding.ItemMovieBinding
import com.example.desafio_ddm.model.Movie

/**
 * Adapter da lista de favoritos. Usa [ListAdapter] + [DiffUtil] para que as
 * edições e exclusões vindas da ViewModel sejam animadas item a item, sem
 * `notifyDataSetChanged`.
 *
 * O acesso às views é feito por View Binding ([ItemMovieBinding]).
 */
class MovieAdapter(
    private val onEditClick: (Movie) -> Unit,
    private val onDeleteClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            val context = binding.root.context

            binding.tvTitulo.text = movie.title
            binding.tvGenero.text = movie.genre
            binding.tvNota.text = context.getString(R.string.nota_formato, movie.rating)
            // A RatingBar tem 5 estrelas e a nota vai de 0 a 10.
            binding.rbNota.rating = movie.rating / 2f

            binding.ivPoster.load(movie.posterUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_movie_placeholder)
                error(R.drawable.ic_movie_placeholder)
            }

            binding.btnEditar.setOnClickListener { onEditClick(movie) }
            binding.btnExcluir.setOnClickListener { onDeleteClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}
