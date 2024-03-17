package com.example.vexaplicao2

import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vexaplicao2.databinding.ActivityDadosPessoaisBinding

private lateinit var binding: ActivityDadosPessoaisBinding

class DadosPessoais : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDadosPessoaisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icSeta.setOnClickListener() {

            val voltarperfil = Intent(this, perfilultilizador::class.java)
            startActivity(voltarperfil)
        }
    }
}