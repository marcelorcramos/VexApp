package com.example.vexaplicao2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vexaplicao2.databinding.ActivityHistoricoBinding


class Historico : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icSeta.setOnClickListener() {
            val voltarMaps= Intent(this, Principal::class.java)
            startActivity(voltarMaps)
        }

    }
}