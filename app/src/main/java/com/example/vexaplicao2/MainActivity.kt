package com.example.vexaplicao2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.vexaplicao2.databinding.ActivityMainBinding
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener() {
            val irLogin = Intent(this, Login::class.java)
            startActivity(irLogin)
        }

        binding.btnCadastro.setOnClickListener(){
            val irCadastro = Intent(this, Cadastro::class.java)
            startActivity(irCadastro)
        }
    }
}