package com.example.desafio_ddm.viewmodel

import androidx.lifecycle.ViewModel

/**
 * ViewModel da tela de login. As credenciais são fixas por ser um MVP
 * acadêmico, mas a regra fica fora da Activity.
 */
class LoginViewModel : ViewModel() {

    fun isValidLogin(user: String, password: String): Boolean =
        user.trim() == VALID_USER && password == VALID_PASSWORD

    private companion object {
        const val VALID_USER = "admin"
        const val VALID_PASSWORD = "1234"
    }
}
