package com.lpennavic.generalboard.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.lpennavic.generalboard.EmailChangeBox
import com.lpennavic.generalboard.LoginActivity
import com.lpennavic.generalboard.PasswordChangeBox
import com.lpennavic.generalboard.R

class SettingsFragment(val userId: String): Fragment(R.layout.fragment_settings) {

    lateinit var settingList: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리스트뷰 어뎁터 바인딩
        settingList = view.findViewById(R.id.settingList)
        val viewItems = ArrayList<String>()
        viewItems.add("비밀번호 변경") // 이메일 인증 필요
        viewItems.add("아이디 변경") // 이메일 인증 필요
        viewItems.add("로그아웃")
        val settingListAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, viewItems)
        settingList.adapter = settingListAdapter
        settingList.setOnItemClickListener { adapterView, view, pos, id ->
            when(pos) {
                0 -> {
                    // 비밀번호 변경
                    // 이메일 인증 필요
                    val intent = Intent(view.context, PasswordChangeBox::class.java)
                    intent.putExtra("user", userId)
                    startActivity(intent)
                }
                1 -> {
                    // 이메일 변경
                    // 이메일 인증 필요
                    // 비밀번호 확인 필요
                    val intent = Intent(view.context, EmailChangeBox::class.java)
                    intent.putExtra("user", userId)
                    startActivity(intent)
                }
                2 -> {
                    // 로그아웃
                    val logoutDialogBuilder = AlertDialog.Builder(view.context)
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예") { dialog, which ->
                            val sharedPreferences = view.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().run {
                                putBoolean("is_logged_in", false) // 로그인 상태를 false로 설정
                                commit()
                            }

                            val intent = Intent(view.context, LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                        .setNegativeButton("아니오") { dialog, which ->
                            dialog.dismiss()
                        }
                    logoutDialogBuilder.show()
                }

            }
        }

    }
}