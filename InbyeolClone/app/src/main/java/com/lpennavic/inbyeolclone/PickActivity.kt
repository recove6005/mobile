package com.lpennavic.inbyeolclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lpennavic.inbyeolclone.databinding.ActivityPickBinding
import com.lpennavic.inbyeolclone.model.PosterModel
import java.text.SimpleDateFormat
import java.util.Date

class PickActivity : AppCompatActivity() {
    lateinit var binding: ActivityPickBinding
    lateinit var storage: FirebaseStorage
    lateinit var photoUri: Uri
    lateinit var auth: FirebaseAuth
    lateinit var fireStore: FirebaseFirestore

    val getPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // 사진 데이터를 받은 후 사후 처리 >> ImageView에 사진을 표시
        result ->
        photoUri = result.data?.data!!
        binding.photoView.setImageURI(photoUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pick)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick)
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        // 사진 가져오기, 가져온 사진 업로드 하기
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getPhoto.launch(intent)

        // 서버에 사진 업로드 및 게시물 업로드
        binding.uploadBtn.setOnClickListener {
            postContents()
        }
    }

    fun postContents() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "img"+timestamp+".png"
        var description = binding.photoEdit.text.toString()

        storage = FirebaseStorage.getInstance()
        val storagePath = storage.reference.child("images").child(fileName)

        // 이미지 경로를 지정 시 직접적으로 노출되기 때문에 보안 상 좋지 않음 >> uri 링크를 통해 다운로드
        storagePath.putFile(photoUri).addOnCompleteListener { uploadTask ->
            if(uploadTask.isSuccessful) {
                storagePath.downloadUrl.addOnCompleteListener { uri ->
                    val downloadUrl = uri.result.toString()

                    // Firestore에 데이터 저장
                    var posterModel = PosterModel().apply {
                        this.description = description
                        imageUrl = downloadUrl
                        uid = auth.uid.toString()
                        userId = auth.currentUser?.email.toString()
                        this.timestamp = System.currentTimeMillis().toString()
                        favCnt = 0
                        favs = mutableMapOf()
                    }
                    fireStore.collection("images").add(posterModel)
                }
            }
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
            finish()
        }


    }
}