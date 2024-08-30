package com.lpennavic.generalboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.Adapter.CommantAdapter
import com.lpennavic.generalboard.DAO.CommantDAO
import com.lpennavic.generalboard.DAO.PosterDAO
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.CommantDTO
import com.lpennavic.generalboard.Interface.OnCommantItemClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PosterActivity : AppCompatActivity(), OnCommantItemClickListener {
    lateinit var backbtn: ImageButton
    lateinit var posterTextPosterTitle: TextView
    lateinit var posterImgProfile: ImageView
    lateinit var posterTextNickname: TextView
    lateinit var posterTextPosterContent: TextView
    lateinit var posterBtnDeleteBtn: Button
    lateinit var posterCommantList: RecyclerView
    lateinit var posterEditCommant: EditText
    lateinit var posterBtnCmtbtn: Button

    var fragmentId : Int = R.id.nav_board

    private lateinit var commantAdapter: CommantAdapter


    @SuppressLint("NotifyDataSetChanged") // notifyDataSetChanted 에 관한 경고를 모두 무시함
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_poster)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 화면 종료 시 MainActivity로 전환할 때 화면 설정을 위한 fragment id
        fragmentId = intent.getIntExtra("fragment_id", R.id.nav_board)
        // val fragmentName = resources.getResourceEntryName(fragmentId)

        // 뒤로가기 버튼
        backbtn = findViewById(R.id.backbtn)
        backbtn.setOnClickListener {
            if(fragmentId == R.id.nav_profile) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment_id", R.id.nav_profile)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 포스터 화면 구성
        val userId = intent.getStringExtra("user_id")
        val user = UserDAO.selectUser(this, userId.toString())
        val posterId = intent.getStringExtra("poster_id").toString()
        val poster = PosterDAO.selectPoster(this, posterId)


        if (poster != null && user!=null) {
            posterTextPosterTitle = findViewById(R.id.poster_text_poster_title)
            posterTextPosterTitle.text = poster.title
            posterImgProfile = findViewById(R.id.poster_img_profile)

            val imgFile = user.profile.toString()
            val profileFile = BitmapFactory.decodeFile(imgFile)
            if (profileFile != null) posterImgProfile.setImageBitmap(profileFile)
            else posterImgProfile.setImageResource(R.drawable.profile_5050)

            posterTextNickname = findViewById(R.id.poster_text_nickname)
            posterTextNickname.text = user.nickname
            posterTextPosterContent = findViewById(R.id.poster_text_content)
            posterTextPosterContent.text = poster.content

            // 포스터 작성자id와 로그인 중인 사용자의 id가 같으면 삭제 버튼을 보이게, 아니면 숨김
            posterBtnDeleteBtn = findViewById(R.id.poster_btn_deletebtn)
            if(user.id.contentEquals(poster.writerId)) posterBtnDeleteBtn.visibility = View.VISIBLE
            else posterBtnDeleteBtn.visibility = View.GONE
            posterBtnDeleteBtn.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                    .setMessage("Would you like to delete this poster?")
                    .setPositiveButton("Yes") { dialog, which ->
                        intent = Intent(this, MainActivity::class.java)
                        // profile fragment에서 넘어온 경우 액티비티 종료 시 다시 profile fragment로 이동
                        if(fragmentId == R.id.nav_profile) intent.putExtra("fragment_id", R.id.nav_profile)

                        // 포스터 삭제
                        PosterDAO.deletePoster(this, poster)
                        // 해당 포스터 댓글들도 모두 삭제
                        CommantDAO.deleteCommantByPosterId(this, posterId)

                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                dialogBuilder.show()
            }
        } else {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "This poster is not exists.", Toast.LENGTH_SHORT).show()
        }

        // 코멘트 리스트 화면 구성
        posterCommantList = findViewById(R.id.poster_list_commant)
        posterCommantList.layoutManager = LinearLayoutManager(this)
        val commants:MutableList<CommantDTO> = CommantDAO.selectCommantByPosterId(this, posterId)
        Log.v("commantcmt", commants.size.toString())
        commantAdapter = CommantAdapter(this, userId.toString(), commants, this)
        posterCommantList.adapter = commantAdapter
        commantAdapter.notifyDataSetChanged()

        // 코멘트 작성 화면 구성
        posterEditCommant = findViewById(R.id.poster_edit_commant)
        posterBtnCmtbtn = findViewById(R.id.poster_btn_cmtbtn)
        posterBtnCmtbtn.setOnClickListener {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val allContent = posterEditCommant.text
            var calledUserId = allContent.toString()
            var content = ""
            // 문자열에 @가 포함될 경우 언급된 사용자의 id를 찾기위해 @와 공백 사이의 문자열 추출
            if(allContent.contains("@")) {
                val firstSpaceIdx = allContent.indexOf(" ")
                calledUserId = allContent.substring(0,firstSpaceIdx) // firstSpaceIdx 직전까지의 문자열 반환, firstSpaceIdx 위치에 있는 문자열은 제외됨
                content = allContent.substring(firstSpaceIdx+1)
            }

            val commantDTO = CommantDTO(0, posterId.toInt(), userId.toString(), content, date, calledUserId)
            CommantDAO.insertCommant(this, commantDTO)
            posterEditCommant.setText(null)

            Toast.makeText(this, "A commant has been commanted.", Toast.LENGTH_SHORT).show()

            val datas = CommantDAO.selectCommantByPosterId(this, posterId)
            val commantAdapter = CommantAdapter(this, userId.toString(), datas, this)
            commantAdapter.notifyDataSetChanged()
            posterCommantList.adapter = commantAdapter
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 백버튼이 눌리면
            fragmentId = intent.getIntExtra("fragment_id", R.id.nav_board)
            if(fragmentId == R.id.nav_profile) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment_id", R.id.nav_profile)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDeleteBtnClick(position: Int) {
        // 어뎁터 항목 제거
        commantAdapter.removeItem(position)
    }
}