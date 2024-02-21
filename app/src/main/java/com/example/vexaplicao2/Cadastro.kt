package com.example.vexaplicao2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vexaplicao2.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth


class Cadastro : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnCadastro.setOnClickListener() { view ->
            val email = binding.editEmail.text.toString()
            val palavrapasse = binding.editPalavraPasse.text.toString()
            val confirmacaopalavrapasse = binding.editPalavraPasseConfirmacao.text.toString()



            if (email.isNotEmpty() && palavrapasse.isNotEmpty() && confirmacaopalavrapasse.isNotEmpty()) {
                if (palavrapasse == confirmacaopalavrapasse) {
                    firebaseAuth.createUserWithEmailAndPassword(email, palavrapasse)
                        .addOnCompleteListener() {
                            if (it.isSuccessful) {
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "As palavras-passe não estão iguais", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Os campos não podem estar vazios", Toast.LENGTH_SHORT).show()
            }
        }
        binding.txtLogin.setOnClickListener() {
            val voltarLogin = Intent(this, Login::class.java)
            startActivity(voltarLogin)
        }
        binding.icSeta.setOnClickListener() {
            val voltarMain = Intent(this, MainActivity::class.java)
            startActivity(voltarMain)
        }
    }
}