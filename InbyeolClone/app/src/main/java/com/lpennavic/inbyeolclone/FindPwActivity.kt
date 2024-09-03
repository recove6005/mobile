package com.lpennavic.inbyeolclone

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.databinding.ActivityFindPwBinding

class FindPwActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindPwBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_pw)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_pw)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.findPwBtn.setOnClickListener {
            findPw()
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    fun findPw() {
        val email = binding.emailEdit.text.toString()
        auth.sendPasswordResetEmail(email)
        Toast.makeText(this, "Password reset mail has been sent.", Toast.LENGTH_SHORT).show()
        finish()
    }
}