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
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PasswordChangeBox : AppCompatActivity() {
    val backgroundScope = CoroutineScope(Dispatchers.Default+ Job())

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

        val emailProveBtn = findViewById<Button>(R.id.email_prove_btn)
        val emailProveEdit = findViewById<EditText>(R.id.email_prove_edit)
        var proveCode = ""

        // 이메일 인증
        emailProveBtn.setOnClickListener {
            proveCode = Random.nextInt(100000, 1000000).toString()
            val emailSender = EmailSender()
            val recipientEmailAddress = user!!.email
            val subject = "General Board : Email Verification"
            val content = "Here is your verification code: $proveCode"

            emailProveEdit.visibility = View.VISIBLE

            backgroundScope.launch {
            // 코루틴 네트워크 스레드
                emailSender.sendEmail(recipientEmailAddress, subject, content)
                withContext(Dispatchers.Main) { }
            }

        }

        okBtn.setOnClickListener {
            // 비밀번호 확인
            val userCurPw = user!!.pw
            if(userCurPw.contentEquals(currentPw) == false) {
                Toast.makeText(this, "현재 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 리스너 함수를 종료
            }

            // 새 비밀번호 확인
            if(newPw.isEmpty() || newPw.contentEquals(newPwCheck) == false) {
                Toast.makeText(this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일 인증번호 확인
            val userInputCode = emailProveEdit.text.toString()
            if(userInputCode.isEmpty() || userInputCode.contentEquals(proveCode) == false) {
                Toast.makeText(this, "이메일 인증번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("비밀번호를 변경하시겠습니까?")
                .setPositiveButton("예") { dialog, which ->
                    UserDAO.updateUser(this, 2, newPw, user)
                    Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("아니오") { dialog, which ->
                    dialog.dismiss()
                }

            dialogBuilder.show()

        }
    }
}