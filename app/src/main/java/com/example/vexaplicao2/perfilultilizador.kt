package com.example.vexaplicao2

import android.app.Dialog
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.vexaplicao2.databinding.ActivityPerfilultilizadorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class perfilultilizador : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilultilizadorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var ImageUri : Uri
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilultilizadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        binding.icSeta.setOnClickListener() {

            val voltarMaps = Intent(this, Principal::class.java)
            startActivity(voltarMaps)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Ultilizadores")
        binding.btnSubmeter.setOnClickListener() {

            
            val profissao = binding.editProfissao.text.toString()
            val carro = binding.editCarro.text.toString()
            val matricula = binding.editMatricula.text.toString()

            val adicional = adicional(profissao, carro, matricula)
            if(adicional != null){

                Toast.makeText(this,"As informações foram adicionadas com sucesso!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
