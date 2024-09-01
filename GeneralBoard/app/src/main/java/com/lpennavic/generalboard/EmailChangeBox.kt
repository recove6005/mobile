package com.lpennavic.generalboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.Sender.EmailSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

class EmailChangeBox : AppCompatActivity() {

    lateinit var newEmailEdit: EditText
    lateinit var emailProveBtn: Button
    lateinit var codeEdit: EditText
    lateinit var currentPwEdit: EditText
    lateinit var checkPwEdit: EditText
    lateinit var cancelBtn: Button
    lateinit var okBtn: Button

    val coroutineScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_email_change_box)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("user").toString()
        val user = UserDAO.selectUser(this, userId)
        var proveCode = ""

        // 이메일 인증
        newEmailEdit = findViewById(R.id.new_email_edit)
        emailProveBtn = findViewById(R.id.email_prove_btn)
        codeEdit = findViewById(R.id.code_edit)

        emailProveBtn.setOnClickListener {
            val newEmail = newEmailEdit.text.toString()
            val emailSender = EmailSender()
            val subject = "General Board : Email Verification"
            proveCode = Random.nextInt(100000, 1000000).toString()
            val content = "Here is your verification code: $proveCode"

            codeEdit.visibility = View.VISIBLE

            // 사용자가 입력한 이메일로 인증번호 전송
            coroutineScope.launch {
                    emailSender.sendEmail(newEmail, subject, content)
            }
            Toast.makeText(this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 취소 버튼
        cancelBtn = findViewById(R.id.cancel_btn)
        cancelBtn.setOnClickListener {
            finish()
        }

        // 확인 버튼
        currentPwEdit = findViewById(R.id.current_pw_edit)
        checkPwEdit = findViewById(R.id.check_pw_edit)
        okBtn = findViewById(R.id.ok_btn)

        okBtn.setOnClickListener {
            // 인증번호 확인
            val userCode = codeEdit.text
            if(!proveCode.contentEquals(userCode)) {
                Toast.makeText(this, "인증번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 확인
            val newEmail = newEmailEdit.text.toString()
            val currentPw = user!!.pw
            val inputPw = currentPwEdit.text
            val checkPw = checkPwEdit.text

            if(inputPw.isEmpty() || checkPw.isEmpty() || !inputPw.contentEquals(checkPw)
                || !inputPw.contentEquals(currentPw)) {
                Toast.makeText(this, "비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("이메일을 변경하시겠습니까?")
                .setPositiveButton("예") {
                        _, _ ->
                    UserDAO.updateUser(this, 5, newEmail, user)
                    Toast.makeText(this, "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("아니오") {
                        d, _ ->
                    d.dismiss()
                }
            dialogBuilder.show()
        }
    }
}