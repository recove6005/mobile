package com.lpennavic.generalboard

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.UserDTO
import java.io.File
import java.io.FileOutputStream

class LoginActivity : AppCompatActivity() {
    lateinit var loginBtn: Button
    lateinit var registerBtn: Button
    lateinit var userIdEdit: EditText
    lateinit var userPwEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        defaultTestUser()

        userIdEdit = findViewById(R.id.userIdEdit)
        userPwEdit = findViewById(R.id.userPwEdit)

        // 앱 시작 시 로그인 상태 확인
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean("is_logged_in", false)) {
            // 이미 로그인 된 상태이므로 메인페이지로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 로그인
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val userId = userIdEdit.text.toString()
            val userPw = userPwEdit.text.toString()

            var user = UserDAO.selectUser(this, userId)
            if(user!=null) {
                if (userPw.contentEquals(user.pw)) {
                    // 올바른 회원정보 입력 시 사용자의 로그인 정보를 저장하고 main 페이지로 이동

                    // 회원 로그인정보 저장 >> SharedPreferences 사용
                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().run {
                        putBoolean("is_logged_in", true)
                        putString("user_id", user.id)
                        commit()
                    }

                    // 메인페이지로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // 비밀번호가 올바르지 않으면 다이얼로그를 띄움
                    val builder = AlertDialog.Builder(this)
                    builder
                        .setMessage("아이디 또는 비밀번호가 올바르지 않습니다.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->  })
                        //.setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->  })
                    val dialog = builder.create()
                    dialog.show()
                }
            } else {
                // 회원정보가 db에 없으면 다이얼로그를 띄움
                val builder = AlertDialog.Builder(this)
                builder
                    .setMessage("회원 정보가 없습니다.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->  })
                    //.setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->  })
                val dialog = builder.create()
                dialog.show()
            }
        }

        // 회원가입 버튼
        registerBtn = findViewById(R.id.registerBtn)
        registerBtn.setOnClickListener {
            // 회원가입 화면으로 이동
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun defaultTestUser() {
        if(UserDAO.selectUser(this, "testuser") == null) {
            val defaultProfileFile = saveDrawableToFile(this, R.drawable.profile_5050, "profile_5050.png")
            val testUser = UserDTO("testuser", "testuser", "1111", defaultProfileFile, "", "leehan6005@gmail.com")
            UserDAO.insertUser(this, testUser)
            Log.v("sqlite", "testuser inserted.")
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