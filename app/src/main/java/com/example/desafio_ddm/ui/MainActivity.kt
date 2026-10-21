package com.example.desafio_ddm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio_ddm.R
import com.example.desafio_ddm.databinding.ActivityMainBinding
import com.example.desafio_ddm.viewmodel.LoginViewModel

/** Tela de login. A View só coleta os campos e reage ao resultado da ViewModel. */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnEntrar.setOnClickListener {
            val usuario = binding.etUsuario.text.toString()
            val senha = binding.etSenha.text.toString()

            if (viewModel.isValidLogin(usuario, senha)) {
                startActivity(Intent(this, ListaActivity::class.java))
            } else {
                Toast.makeText(this, R.string.erro_login, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
