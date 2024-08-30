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
import com.lpennavic.generalboard.DAO.PosterDAO
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.PosterDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostActivity : AppCompatActivity() {

    lateinit var postEditTitle: EditText
    lateinit var postEditContent: EditText
    lateinit var postBtnpost: Button
    lateinit var postBtnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        postEditTitle = findViewById(R.id.post_edit_title)
        postEditContent =findViewById(R.id.post_edit_content)
        postBtnpost = findViewById(R.id.post_btn_post)
        postBtnCancel = findViewById(R.id.post_btn_cancel)

        var intent = intent

        val user = UserDAO.selectUser(this, intent.getStringExtra("user").toString())
        if(user==null) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        postBtnpost.setOnClickListener {
            val writerId = user?.id.toString()
            val writerNickname = user?.nickname.toString()
            val title = postEditTitle.text.toString()
            val content = postEditContent.text.toString()
            // val date = LocalDate.now() >> API 16 이하에서는 사용 불가
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val posterDTO = PosterDTO(0, writerId, writerNickname, title, content, date)
            PosterDAO.insertPoster(this, posterDTO)

            Toast.makeText(this, "Poster has been posted.", Toast.LENGTH_SHORT)

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        postBtnCancel.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}