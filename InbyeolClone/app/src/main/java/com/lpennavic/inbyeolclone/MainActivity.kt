package com.lpennavic.inbyeolclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lpennavic.inbyeolclone.Fragment.AlarmFragment
import com.lpennavic.inbyeolclone.Fragment.DetailViewFragment
import com.lpennavic.inbyeolclone.Fragment.GridFragment
import com.lpennavic.inbyeolclone.Fragment.UserFragment
import com.lpennavic.inbyeolclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomNav.setOnNavigationItemSelectedListener(this)

        // 권한 요청
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                val f = DetailViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()
                return true
            }
            R.id.action_search -> {
                val f = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()
                return true
            }
            R.id.action_photo -> {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    // 퍼미션 허용
                    startActivity(Intent(this, PickActivity::class.java))
                } else {
                    // 퍼미션 거부
                    Toast.makeText(this, "파일 읽기 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        0)
                }
            }
            R.id.action_alarm -> {
                val f = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()
                return true
            }
            R.id.action_account -> {
                val f = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()
                return true
            }
        }
        return false
    }
}