package com.lpennavic.generalboard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO

class IdChangeBox : AppCompatActivity() {
    lateinit var newIdEdit: EditText
    lateinit var currentPwEdit: EditText
    lateinit var checkPwEdit: EditText
    lateinit var cancelBtn: Button
    lateinit var okBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_id_change_box)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("user").toString()
        val user = UserDAO.selectUser(this, userId)
        val userPw = user!!.pw

        newIdEdit = findViewById(R.id.new_id_edit)
        currentPwEdit = findViewById(R.id.current_pw_edit)
        checkPwEdit = findViewById(R.id.check_pw_edit)
        cancelBtn = findViewById(R.id.cancel_btn)
        okBtn = findViewById(R.id.ok_btn)

        cancelBtn.setOnClickListener {
            finish()
        }

        okBtn.setOnClickListener {
            val newId = newIdEdit.text.toString()
            val currentPw = currentPwEdit.text.toString()
            val checkPw = checkPwEdit.text.toString()

            if(newId.isEmpty()) {
                Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            if(currentPw.isEmpty() || checkPw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            if(currentPw.contentEquals(userPw) == false) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            // 아이디 중복 체크
            val tempUser = UserDAO.selectUser(this, newId)
            if(tempUser != null) {
                Toast.makeText(this, "현재 사용중인 아이디 입니다.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            // 아이디 변경
            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("아이디를 변경하시겠습니까?")
                .setPositiveButton("예") { dialog, which ->
                    // UserDAO.updateUser(this, )
                }
                .setNegativeButton("아니오") { dialog, which ->

                }

        }


    }
}