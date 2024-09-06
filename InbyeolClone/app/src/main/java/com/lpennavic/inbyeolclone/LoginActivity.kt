package com.lpennavic.inbyeolclone

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.lpennavic.inbyeolclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var googleSigninClient: GoogleSignInClient

    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> // 구글 로그인 성공 시 암호화(Token)된 결과값(email)을 Firebase 서버에 전송

        // 구글 로그인 액티비티로부터 반환된 데이터를 포함하는 Intent 객체를 가져옴
        var data = result.data // Token값이 담긴 Intent

        // data(Intent)로부터 GoogleSignInAccount 객체를 가져오는 Task 객체를 반환
        // task 객체는 비동기 작업을 나타내며, 작업이 성공하거나 실패할 때 처리할 수 있는 API를 제공
        var task = GoogleSignIn.getSignedInAccountFromIntent(data)

        // task 결과를 가져옴 GoogleSignInAccount(사용자의 구글 계정 정보)객체를 반환
        // 실패시 ApiException 객체를 던짐
        var account = task.getResult(ApiException::class.java)

        firebaseAuthWithGoogle(account.idToken)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        // 자동로그인
        moveToMain(auth.currentUser)
        // auth.signOut() >> 세션을 날리는 코드, 로그아웃 됨

        // 이메일 로그인
        binding.emailLoginBtn.setOnClickListener {
            signupAndSignin()
        }

        // 구글 로그인
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build() // 구글 요청 정보 정의

        // GoogleSignInClient(실제로 구글 로그인 프로세스를 처리) 객체를 생성
        // 구글 요청 정보 gso에 따라 로그인 프로세스 진행
        googleSigninClient = GoogleSignIn.getClient(this, gso)
        binding.googleLoginBtn.setOnClickListener {
            googleLogin()
        }

        // 페이스북 로그인
        binding.facebookLoginBtn.setOnClickListener {

        }

        // 아이디 찾기
        binding.findIdBtn.setOnClickListener {
            startActivity(Intent(this, FindIdActivity::class.java))
        }

        // 비밀번호 찾기
        binding.findPwBtn.setOnClickListener {
            startActivity(Intent(this, FindPwActivity::class.java))
        }
    }

    fun signupAndSignin() {
        val id = binding.editId.text.toString()
        val pw = binding.editPw.text.toString()
        auth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                // 계정이 없을 경우 계정 생성
                // 생성된 계정 정보(email, phoneNumber)를 firesotre에 저장
                findAndSaveData()
            } else {
                // 이미 계정이 존재함
                signinByEmail()
            }
        }
    }

    fun signinByEmail() {
        val id = binding.editId.text.toString()
        val pw = binding.editPw.text.toString()
        auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                // 계정 생성 성공 -> 메인 화면으로 이동
                moveToMain(task.result?.user)
            } else {
                // 이메일 로그인 실패

            }
        }
    }

    fun findAndSaveData() {
        startActivity(Intent(this, InputNumberActivity::class.java))
        finish()
    }

    fun googleLogin() {
        // 기존에 생성한 GoogleSigninClient 객체로부터 로그인을 위한 Intent 객체를 가져옴
        var i = googleSigninClient.signInIntent
        // intent 호출 후 결과를 반환받음
        googleLoginResult.launch(i)
    }

    fun firebaseAuthWithGoogle(idToken: String?) {
        var credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
                task ->
            if(task.isSuccessful) {
                if(auth.currentUser!!.isEmailVerified) {
                    // 이메일 인증이 완료 된 경우
                    moveToMain(auth.currentUser)
                } else {
                    // 이메일 인증이 안 된 경우
                    findAndSaveData()
                }
            }
        }
    }

    fun moveToMain(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}