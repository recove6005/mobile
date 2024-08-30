package com.lpennavic.generalboard.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.lpennavic.generalboard.Adapter.PosterAdapter
import com.lpennavic.generalboard.DAO.PosterDAO
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.PosterDTO
import com.lpennavic.generalboard.PostActivity
import com.lpennavic.generalboard.PosterActivity
import com.lpennavic.generalboard.R
import java.time.LocalDate

class BoardFragment(userId: String): Fragment(R.layout.fragment_board) {
    val userId = userId

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // UI 작업
        // 글 쓰기
        // 현재 로그인 중인 사용자 정보 가져오기
        val user = UserDAO.selectUser(view.context, userId)

        // 전체 게시글을 불러와 게시판 화면 전체에 뿌리기
        var posters: MutableList<PosterDTO>? = PosterDAO.selectAllPoster(view.context)
        var boardList = view.findViewById<ListView>(R.id.board_list)
        if(posters!=null) {
            // 커스텀 어뎁터 바인딩
            val boardAdapter = PosterAdapter(view.context, R.layout.listitem_board, posters)
            boardList.adapter = boardAdapter
        }

        val boardPostBtn = view.findViewById<ImageButton>(R.id.board_post_btn)
        boardPostBtn.setOnClickListener {
            // 게시글 등록
            // 게시글 id > auto increment
            if(user!=null) {
                val writerId = user.id
                val writerNickname = user.nickname

                val intent = Intent(view.context, PostActivity::class.java)
                intent.putExtra("user", user.id)
                startActivity(intent)
                // fragment 내에서 parent activity 종료하기
                // requireActivity().finish()

                // val title = "title"
                // val content = "content"
                // val date = LocalDate.now().toString()
                // val posterDTO = PosterDTO(0, writerId, writerNickname, title, content, date)
                // PosterDAO.insertPoster(view.context, posterDTO)
                // 데이터 변경 후 변경 사항을 어뎁터 및 UI에 적용
                // posters = PosterDAO.selectAllPoster(view.context)
                // posters?.get(0)?.let { it1 -> Log.v("sqliteposter", it1.writerId + it1.title) }
                // val tmpAdapter = boardList.adapter as PosterAdapter
                // tmpAdapter.notifyDataSetChanged()

            }
        }

        // 포스터 항목 클릭 시 PosterActivity로 이동
        // 해당 포스터id, 로그인 중인 유저id
        boardList.setOnItemClickListener { parent, view, position, id ->
            // parent : 클릭된 항목이 포함된 AdapterView(보통 ListView)
            // view : 클릭된 항목, 레이아웃 참조 가능 >> view.setBackgroundColor(Color.RED)
            // position : 클릭된 항목의 위치(index)
            // id : 클릭된 항목의 id, 기본값일 경우 position과 같은 값이거나 -1
            // 항목 데이터 가져오기 >> listview.getItemAtPosition(position)

            val item = view.findViewById<TextView>(R.id.poster_id)

            val intent = Intent(view.context, PosterActivity::class.java)
            intent.putExtra("poster_id", "${item.text}")
            intent.putExtra("user_id", "$userId")
            startActivity(intent)
            activity?.finish()
        }
    }
}