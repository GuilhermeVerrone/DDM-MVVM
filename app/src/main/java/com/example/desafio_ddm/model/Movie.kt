package com.example.desafio_ddm.model

/**
 * Model (camada M do MVVM): representa um filme favorito do usuário.
 *
 * O [id] é gerado pelo repositório e serve para localizar o filme na edição
 * e na exclusão, sem depender da posição dele na lista.
 */
data class Movie(
    val id: Long,
    val title: String,
    val genre: String,
    val rating: Float,
    val posterUrl: String
) {
    companion object {
        const val MIN_RATING = 0f
        const val MAX_RATING = 10f
    }
}
