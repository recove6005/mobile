package com.lpennavic.generalboard

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lpennavic.generalboard.Fragment.BoardFragment
import com.lpennavic.generalboard.Fragment.LetterFragment
import com.lpennavic.generalboard.Fragment.NotificationFragment
import com.lpennavic.generalboard.Fragment.ProfileFragment
import com.lpennavic.generalboard.Fragment.SettingsFragment

class MainActivity : AppCompatActivity() {
    var initTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "")
        val fragmentId = intent.getIntExtra("fragment_id", R.id.nav_board)

        // BottomNavigationView와 네비게이션 설정
        val menubar = findViewById<BottomNavigationView>(R.id.menu_bar)

        if (savedInstanceState == null) {
            menuIconAllFree(menubar)
            when (fragmentId) {
                R.id.nav_board -> {
                    loadFragment(BoardFragment(userId.toString()))
                    menubar.selectedItemId = R.id.nav_board
                    menubar.menu.findItem(R.id.nav_board).setIcon(R.drawable.nav_board_clicked)
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(userId.toString()))
                    menubar.selectedItemId = R.id.nav_profile
                    menubar.menu.findItem(R.id.nav_profile).setIcon(R.drawable.nav_profile_clicked)
                }
                R.id.nav_letter -> {
                    loadFragment(LetterFragment())
                    menubar.selectedItemId = R.id.nav_letter
                    menubar.menu.findItem(R.id.nav_letter).setIcon(R.drawable.nav_letter_clicked)
                }
                R.id.nav_notification -> {
                    loadFragment(NotificationFragment())
                    menubar.selectedItemId = R.id.nav_notification
                    menubar.menu.findItem(R.id.nav_notification).setIcon(R.drawable.nav_bell_clicked)
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment(userId.toString()))
                    menubar.selectedItemId = R.id.nav_settings
                    menubar.menu.findItem(R.id.nav_settings).setIcon(R.drawable.nav_gear_clicked)
                }
                else -> loadFragment(BoardFragment(userId.toString()))
            }
        }

        menubar.setOnItemSelectedListener { item ->
            menuIconAllFree(menubar)
            when(item.itemId) {
                    R.id.nav_board -> {
                        loadFragment(BoardFragment(userId.toString()))
                        menubar.menu.findItem(R.id.nav_board).setIcon(R.drawable.nav_board_clicked)
                    }
                    R.id.nav_profile -> {
                        loadFragment(ProfileFragment(userId.toString()))
                        menubar.menu.findItem(R.id.nav_profile).setIcon(R.drawable.nav_profile_clicked)
                    }
                    R.id.nav_letter -> {
                        loadFragment(LetterFragment())
                        menubar.menu.findItem(R.id.nav_letter).setIcon(R.drawable.nav_letter_clicked)
                    }
                    R.id.nav_notification -> {
                        loadFragment(NotificationFragment())
                        menubar.menu.findItem(R.id.nav_notification).setIcon(R.drawable.nav_bell_clicked)
                    }
                    R.id.nav_settings -> {
                        loadFragment(SettingsFragment(userId.toString()))
                        menubar.menu.findItem(R.id.nav_settings).setIcon(R.drawable.nav_gear_clicked)
                    }
            }
            true
        }
    }

    // 메뉴바 아이템 프래그먼트 처리
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    // 백버튼 키 이벤트 처리 : 사용자가 백버튼 실행 시 뒤로 가지 않고 앱을 종료하도록 설정
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode === KeyEvent.KEYCODE_BACK) {
            // 백버튼이 눌리면
            if(System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "Press the back button once more to exit this app.", Toast.LENGTH_SHORT).show()
                initTime = System.currentTimeMillis()
                return true
            }
            finishAffinity() // 현재 액티비티와 같은 태스크에 있는 모든 액티비티 종료
            finishAndRemoveTask() // 현재 태스크를 완전히 종료하고 제거
        }
        return super.onKeyDown(keyCode, event)
    }

    fun menuIconAllFree(menubar: BottomNavigationView) {
        menubar.menu.findItem(R.id.nav_board).setIcon(R.drawable.nav_board_free)
        menubar.menu.findItem(R.id.nav_profile).setIcon(R.drawable.nav_profile_free)
        menubar.menu.findItem(R.id.nav_letter).setIcon(R.drawable.nav_letter_free)
        menubar.menu.findItem(R.id.nav_notification).setIcon(R.drawable.nav_bell_free)
        menubar.menu.findItem(R.id.nav_settings).setIcon(R.drawable.nav_gear_free)
    }
}