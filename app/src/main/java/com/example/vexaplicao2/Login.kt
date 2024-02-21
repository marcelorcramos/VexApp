package com.example.vexaplicao2


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.vexaplicao2.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(){ view ->
            val email = binding.editEmail.text.toString()
            val palavrapasse = binding.editPalavraPasse.text.toString()

            if(email.isNotEmpty() && palavrapasse.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,palavrapasse).addOnCompleteListener(){
                    if(it.isSuccessful){
                        val loginconcluido = Intent (this,Principal::class.java)
                        startActivity(loginconcluido)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Campos não podem estar vazios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtEsqueceu.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.esqueceupp2, null)
            val ultilizadorEmail = view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnResetar).setOnClickListener(){
                compareEmail(ultilizadorEmail)
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        binding.txtCadastrese.setOnClickListener() {
            val irCadastro = Intent(this, Cadastro::class.java)
            startActivity(irCadastro)
        }

        binding.icSeta.setOnClickListener(){
            val voltarMain = Intent(this, MainActivity::class.java)
            startActivity(voltarMain)
        }
    }
    private fun compareEmail(email: EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener(){task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Olhe seu email",Toast.LENGTH_SHORT).show()
            }
        }
    }

}