package com.lpennavic.generalboard

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lpennavic.generalboard.DAO.UserDAO

class NicknameChangeBox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nickname_change_box)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("user").toString()
        val user = UserDAO.selectUser(this, userId)

        val newNicknameEdit = findViewById<EditText>(R.id.nickname_eidt)
        val newNickname = newNicknameEdit.text
        val okBtn = findViewById<Button>(R.id.ok_btn)
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)

        cancelBtn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment_id", R.id.nav_profile)
            startActivity(intent)
            finish()
        }

        okBtn.setOnClickListener {
            UserDAO.updateUser(this, 1, newNickname, user!!)
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment_id", R.id.nav_profile)
            startActivity(intent)
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 백버튼이 눌리면
            val fragmentId = intent.getIntExtra("fragment_id", R.id.nav_board)
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment_id", R.id.nav_profile)
            startActivity(intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}