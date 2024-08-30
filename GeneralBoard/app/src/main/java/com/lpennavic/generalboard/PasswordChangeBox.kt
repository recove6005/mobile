package com.lpennavic.generalboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO

class PasswordChangeBox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password_change_box)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("user").toString()
        val user = UserDAO.selectUser(this, userId)

        val currentPwEdit = findViewById<EditText>(R.id.current_pw_edit)
        val currentPw = currentPwEdit.text
        val newPwEdit = findViewById<EditText>(R.id.new_pw_edit)
        val newPw = newPwEdit.text
        val newPwCheckEdit = findViewById<EditText>(R.id.new_pwcheck_edit)
        val newPwCheck = newPwCheckEdit.text
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)
        val okBtn = findViewById<Button>(R.id.ok_btn)

        cancelBtn.setOnClickListener {
            finish()
        }

        okBtn.setOnClickListener {
            val userCurPw = user!!.pw
            if(userCurPw.contentEquals(currentPw) == false) {
                Toast.makeText(this, "현재 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 리스너 함수를 종료
            }

            if(newPw.contentEquals(newPwCheck)== false) {
                Toast.makeText(this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserDAO.updateUser(this, 2, newPw, user)

            Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}