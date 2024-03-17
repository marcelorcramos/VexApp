package com.example.vexaplicao2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vexaplicao2.databinding.ActivityHistoricoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Historico : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MarkerClicks", MODE_PRIVATE)
        val clickCount = sharedPreferences.getInt("clickCount", 0)
        val lastClickTimestamp = sharedPreferences.getLong("lastClickTimestamp", 0)
        val lastClickedMarker = sharedPreferences.getString("lastClickedMarker", "Nenhum marcador clicado")
        val lastClickedMarkerType = sharedPreferences.getString("lastClickedMarkerType", "Nenhum marcador clicado")
        val lastClickDate = Date(lastClickTimestamp)
        val dateFormat = SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault())
        val lastClickTimeString = dateFormat.format(lastClickDate)

        binding.textViewClickCount.text = "Concluídos: $clickCount"
        binding.textViewLastClickedMarker.text = "Último ponto de carregamento:\n $lastClickedMarker"
        binding.textViewLastClickedMarkerInfo.text = "\tÚltimo ponto de carregamento:\n $lastClickedMarkerType"
        binding.textinformacaoadicional.text = "\tHorário do último carregamento:\n $lastClickTimeString"

        binding.icSeta.setOnClickListener() {
            val voltarMaps = Intent(this, Principal::class.java)
            startActivity(voltarMaps)
        }

        binding.btnirutilizador.setOnClickListener() {
            val irutilizador = Intent(this, perfilultilizador::class.java)
            startActivity(irutilizador)
        }

        binding.btnLimparCliques.setOnClickListener {

            val sharedPreferences = getSharedPreferences("MarkerClicks", MODE_PRIVATE)
            val editor = sharedPreferences.edit()


            editor.putInt("clickCount", 0)
            editor.putString("lastClickedMarker", "")
            editor.putString("lastClickedMarkerType","")
            editor.putLong("lastClickTimestamp", 0)
            editor.apply()

            atualizarInterface()
        }
    }

        fun atualizarInterface() {
            val sharedPreferences = getSharedPreferences("MarkerClicks", MODE_PRIVATE)
            val clickCount = sharedPreferences.getInt("clickCount", 0)
            val lastClickedMarker = sharedPreferences.getString("lastClickedMarker", "Nenhum marcador clicado")
            val lastClickedMarkerType = sharedPreferences.getString("lastClickedMarkerType", "Nenhum marcador clicado")
            val lastClickTimestamp = sharedPreferences.getLong("lastClickTimestamp", 0)
            val lastClickDate = Date(lastClickTimestamp)
            val dateFormat = SimpleDateFormat("", Locale.getDefault())
            val lastClickTimeString = dateFormat.format(lastClickDate)

            binding.btnLimparCliques.text = "Concluidos: 0$clickCount"
            binding.textViewLastClickedMarker.text = "Último ponto de carregamento: $lastClickedMarker"
            binding.textViewLastClickedMarkerInfo.text = "\tTipo de carregador: $lastClickedMarkerType"
            binding.textinformacaoadicional.text = "\tHorário do último carregamento: $lastClickTimeString"
        }
    }
