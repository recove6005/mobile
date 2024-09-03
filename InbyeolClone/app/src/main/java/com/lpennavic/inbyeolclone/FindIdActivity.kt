package com.lpennavic.inbyeolclone

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.databinding.ActivityFindIdBinding
import com.lpennavic.inbyeolclone.model.FindIdModel

class FindIdActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindIdBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    var findTypeFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_id)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_id)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.findByEmailText.setOnClickListener {
            binding.phoneEdit.visibility = View.GONE
            binding.emailEdit.visibility = View.VISIBLE
            binding.findByEmailText.visibility = View.GONE
            binding.findByPhoneText.visibility = View.VISIBLE
            findTypeFlag = false
        }
        binding.findByPhoneText.setOnClickListener {
            binding.phoneEdit.visibility = View.VISIBLE
            binding.emailEdit.visibility = View.GONE
            binding.findByEmailText.visibility = View.VISIBLE
            binding.findByPhoneText.visibility = View.GONE
            findTypeFlag = true
        }

        // finding email id
        binding.findIdBtn.setOnClickListener {
            readId()
        }

        // close
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    fun readId() {
        if(findTypeFlag == false) {
            // find id by email
            val email = binding.emailEdit.text.toString()
            firestore.collection("findids").whereEqualTo("id", email).get()
                .addOnCompleteListener {
                    task ->
                    var findIdModel = task.result?.documents?.first()!!.toObject(FindIdModel::class.java)
                    Toast.makeText(this, findIdModel!!.id, Toast.LENGTH_SHORT).show()
                    finish()
                }
        } else {
            // find id by phone number
            val phone = binding.phoneEdit.text.toString()
            firestore.collection("findids").whereEqualTo("phoneNumber", phone).get()
                .addOnCompleteListener {
                    task ->
                    if(task.isSuccessful) {
                        // 아이디 찾기 성공 >> findIdModel에 유저 정보를 끼워맞춤
                        var findIdModel = task.result?.documents?.first()!!.toObject(FindIdModel::class.java)
                        Toast.makeText(this, findIdModel!!.id, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }
    }
}