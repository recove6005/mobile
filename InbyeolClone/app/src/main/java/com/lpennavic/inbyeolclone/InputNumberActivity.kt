package com.lpennavic.inbyeolclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.databinding.ActivityInputNumberBinding
import com.lpennavic.inbyeolclone.model.FindIdModel

class InputNumberActivity : AppCompatActivity() {
    lateinit var binding: ActivityInputNumberBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input_number)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_number)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.applyBtn.setOnClickListener {
            savePhoneNumber()
        }
    }

    fun savePhoneNumber() {
        var findIdModel = FindIdModel()
        findIdModel.id = auth.currentUser?.email
        findIdModel.phoneNumber = binding.editPhone.text.toString()

        // 파이어스토어 데이터베이스에 생성한 findIdModel 정보를 저장
        firestore.collection("findids").document().set(findIdModel)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    // 이메일 인증
                    auth.currentUser?.sendEmailVerification()
                    Toast.makeText(this, "Email verification eamil has been sent.", Toast.LENGTH_SHORT).show()
                    // 로그인 액티비티로 이동
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }


    }
}