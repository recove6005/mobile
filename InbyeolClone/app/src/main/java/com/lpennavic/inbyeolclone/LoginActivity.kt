package com.lpennavic.inbyeolclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lpennavic.inbyeolclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.emailLoginBtn.setOnClickListener {
            signinAndSignup()
        }

    }

    fun signinAndSignup() {
        var id = binding.editId.text.toString()
        var pw = binding.editPw.text.toString()
        firebaseAuth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener {
            task ->
            if(task.isComplete) {
                // 계정 생성을 성공했을 때 >> ID 생성 후 메인 액티비티로 이동
                moveToMain(task.result?.user)
            } else {
                // 이미 계정이 있을 경우
                signinEmail()
            }
        }
    }

    fun moveToMain(user: FirebaseUser?) {
        // 자동로그인
        if(user != null) {
            // firebase 유저가 있을 경우
            // user가 null값일 경우 main으로 이동하지 않기 때문에 자동로그인 불가
            if(user.isEmailVerified) {
                // 이메일 인증이 완료된 유저일 경우
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // 이메일 인증 메일 전송
                user.sendEmailVerification()
                Toast.makeText(this,
                    "A verification email has been sent. Please check your email",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signinEmail() {
        var id = binding.editId.text.toString()
        var pw = binding.editPw.text.toString()
        firebaseAuth.signInWithEmailAndPassword(id, pw).addOnCompleteListener {
            task ->
            if(task.isComplete) {
                moveToMain(task.result?.user)
            } else {


            }
        }
    }
}