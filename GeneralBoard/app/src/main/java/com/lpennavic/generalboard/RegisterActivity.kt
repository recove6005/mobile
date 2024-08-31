package com.lpennavic.generalboard

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.UserDTO
import com.lpennavic.generalboard.Sender.EmailSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random


class RegisterActivity : AppCompatActivity() {
    lateinit var userIdEdit: EditText
    lateinit var userPwEdit: EditText
    lateinit var userNickEdit: EditText
    lateinit var userEmailEdit: EditText
    lateinit var userEmailProveEdit: EditText
    lateinit var emailProveBtn: Button
    lateinit var codeSendBtn: Button
    lateinit var registerBtn: Button
    lateinit var userIdCheckBtn: Button

    var provCode = ""
    var emailCheckFlag = false
    var idCheckFlag = false

    val backgroundScope = CoroutineScope(Dispatchers.Default+ Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userIdEdit = findViewById(R.id.userIdEdit)
        // ID EditText 변경 시 바로 idCheckFlag를 변경
        userIdEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 문자열이 변경되기 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 문자열이 변경될 때 > flag를 false로 변경하여 중복확인을 다시 하게끔 하기
                // 중복확인 버튼을 다시 흰색으로 변경
                idCheckFlag = false
                userIdCheckBtn.setBackgroundColor(Color.WHITE)
            }
            override fun afterTextChanged(s: Editable?) {
                // 문자열이 변경된 후

            }
        })
        userPwEdit = findViewById(R.id.userPwEdit)
        userNickEdit = findViewById(R.id.userNickEdit)
        userEmailEdit = findViewById(R.id.userEmailEdit)
        // Email EditText 변경 시 바로 emailCheckFlag를 변경
        userEmailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 문자열이 변경되기 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 문자열이 변경될 때 > flag를 false로 변경하여 중복확인을 다시 하게끔 하기
                // 중복확인 버튼을 다시 흰색으로 변경, 인증코드 초기화
                emailCheckFlag = false
                provCode = ""
                emailProveBtn.setBackgroundColor(Color.WHITE)
            }
            override fun afterTextChanged(s: Editable?) {
                // 문자열이 변경된 후
            }
        })

        // ID 중복체크
        userIdCheckBtn = findViewById(R.id.idCheckBtn)
        userIdCheckBtn.setOnClickListener {
            val userId = userIdEdit.text.toString()
            val user = UserDAO.selectUser(this, userId)
            if(user!=null) {
                // ID를 사용하는 유저가 이미 존재함, ID중복 > 토스트메시지 띄우기
                Toast.makeText(this, "이미 사용 중인 아이디 입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // ID가 중복되지 않으므로 중복체크 버튼을 빨간색으로 변경, Flag를 true로 변경
                idCheckFlag = true
                userIdCheckBtn.setBackgroundColor(Color.RED)
            }
        }

        // 이메일 인증 코드 보내기
        // 구글 SMTP(simple mail transfer protocool) 이용
        // 이메일 인증 체크
        // 종속성 추가 : java.mail, activation
        // 인터넷 권한 추가
        // <uses-permission android:name="android.permission.INTERNET" />
        // <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        codeSendBtn = findViewById(R.id.codeSendBtn)
        codeSendBtn.setOnClickListener {
            // 이메일 인증 플래그 초기화
            // 인증번호 초기화
            emailCheckFlag = false
            provCode = ""

            val emailSender = EmailSender()
            val recipientEmailAddress = userEmailEdit.text.toString()
            provCode = Random.nextInt(100000, 1000000).toString()
            codeSendBtn.setBackgroundColor(Color.RED)

            val subject = "General Board : Email Verification"
            val content = "Here is your verification code: $provCode"

            // 네트워크 작업
            // 코틀린에서 AsynkTask는 권장되지 않고 대신 Coroutines 사용
            backgroundScope.launch {
                emailSender.sendEmail(recipientEmailAddress, subject, content)
                withContext(Dispatchers.Main) {
                    // coroutines에서 Main 디스패처에 의뢰할 작업들
                }
            }
        }

        // 이메일 인증
        emailProveBtn = findViewById(R.id.emailProveBtn)
        userEmailProveEdit = findViewById(R.id.userEmailProveEdit)
        emailProveBtn.setOnClickListener {
            val userProvCode = userEmailProveEdit.text
            if(userProvCode.contentEquals(provCode)) {
                // 인증번호 일치
                Toast.makeText(this, "Email verification has been completed.", Toast.LENGTH_SHORT)
                Log.v("email prv","email provation successfully.")
                emailProveBtn.setBackgroundColor(Color.RED)
                provCode = ""
                emailCheckFlag = true
            } else {
                // 인증번호 불일치
                Toast.makeText(this, "Please check your verification number.", Toast.LENGTH_SHORT)
                Log.v("email prv","email provation is failed.")
                emailCheckFlag = false
            }
        }

        // 회원가입 버튼
        registerBtn = findViewById(R.id.registerBtn)
        registerBtn.setOnClickListener {
            if(emailCheckFlag && idCheckFlag) {
                // id중복체크 플래그와 이메일 인증 플래그 모두 true이면
                // 회원가입을 하시겠습니까 확인 다이얼로그 띄우기
                val builder = AlertDialog.Builder(this)
                builder
                    .setMessage("회원가입 하시겠습니까?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        val userId = userIdEdit.text.toString()
                        val userPw = userPwEdit.text.toString()
                        val userNickname = userNickEdit.text.toString()
                        val userEmailAddress = userEmailEdit.text.toString()

                        // DB에 유저 등록
                        val defaultProfileFile = saveDrawableToFile(this, R.drawable.profile_5050, "profile_5050.png")
                        val userDTO = UserDTO(userId, userNickname, userPw, defaultProfileFile, "", userEmailAddress)
                        UserDAO.insertUser(this, userDTO)

                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT)
                        onBackPressed()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        idCheckFlag = false
                        emailCheckFlag = false
                        provCode = ""
                        userIdCheckBtn.setBackgroundColor(Color.WHITE)
                        emailProveBtn.setBackgroundColor(Color.WHITE)
                        Toast.makeText(this, "인증을 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    })
                val dialog = builder.create()
                dialog.show()
            } else {
                Log.v("register", "register is failed.")
            }
        }
    }

    fun saveDrawableToFile(context: Context, drawableId: Int, fileName: String): File {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}