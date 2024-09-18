package com.lpennavic.inbyeolclone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.databinding.ActivityProfileBinding
import com.lpennavic.inbyeolclone.databinding.ListitemProfileBinding
import com.lpennavic.inbyeolclone.model.FollowModel
import com.lpennavic.inbyeolclone.model.PosterModel

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var user: String
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    var datas: ArrayList<PosterModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        // 프로필 대상 구분
        user = intent.getStringExtra("user").toString()

        // 사용자 본인의 프로필일 경우 팔로우 버튼 숨김
        if(user.contentEquals(auth.currentUser!!.uid)) binding.followBtn.visibility = View.GONE

        // 프로필 대상 포스팅 정보 가져오기
        firestore.collection("images").whereEqualTo("uid", user)
            .get().addOnSuccessListener { docs ->
                for (doc in docs) {
                    val posterModel = doc.toObject(PosterModel::class.java)
                    datas.add(posterModel)
                }
                binding.postCnt.text = "POST\n${datas.size}" // 포스팅 개수 표시
                binding.photoList.adapter = ProfileViewAdapter()
                binding.photoList.layoutManager = GridLayoutManager(this, 3)
            }
            .addOnFailureListener{ e ->
                Log.w("hanilog", "Error getting documents: ", e)
            }
        // 팔로워 팔로잉 수 표시
        firestore.collection("follow").document(user).get().addOnSuccessListener {
            result ->
            val followModel = result.toObject(FollowModel::class.java)
            if(followModel != null) {
                binding.followerCnt.text = "FOLLOWER\n${followModel.followerCnt}"
                binding.followingCnt.text = "FOLLOWING\n${followModel.followingCnt}"
            }
        }

        // 프로필 이미지 표시
        getProfileImg()

        // 백버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 팔로우 버튼 이벤트
        binding.followBtn.setOnClickListener {
            val leaderDocRef = firestore.collection("follow").document(user)
            val followerDocRef = firestore.collection("follow").document(auth.currentUser!!.uid)
            firestore.runTransaction { transaction ->
                var leaderSnapshot = transaction.get(leaderDocRef)
                if(leaderSnapshot == null) {
                    val followModel = FollowModel()
                    firestore.collection("follow").document(user).set(followModel)
                    leaderSnapshot = transaction.get(leaderDocRef)
                }
                val leader = leaderSnapshot.toObject(FollowModel::class.java)

                var followerSnapshot = transaction.get(followerDocRef)
                if(followerSnapshot == null) {
                    val followModel = FollowModel()
                    firestore.collection("follow").document(auth.currentUser!!.uid).set(followModel)
                    followerSnapshot = transaction.get(leaderDocRef)
                }
                val follower = followerSnapshot.toObject(FollowModel::class.java)

                if(leader != null && follower != null) {
                    // 로그인 중인 사용자가 이미 팔로우 중인 유저의 경우 >> 팔로우 취소
                    if(leader.followers.containsKey(auth.currentUser!!.uid)) {
                        // 상대방
                        leader.followerCnt-=1
                        leader.followers.set(auth.currentUser!!.uid, false)
                        binding.followerCnt.text = "FOLLOW\n${leader.followerCnt}"

                        // 사용자
                        follower.followingCnt-=1
                        follower.followers.set(user, false)
                    } else {
                        // 로그인 중인 사용자가 새롭게 팔로우 하는 경우 >> 팔로우
                        // 상대방
                        leader.followerCnt+=1
                        leader.followers.set(auth.currentUser!!.uid, true)

                        // 사용자
                        follower.followingCnt+=1
                        follower.followers.set(user, true)
                    }
                }
            }
        }
    }

    fun getProfileImg() {
        // 프로필 대상 프로필 사진 가져오기
        firestore.collection("profiles").whereEqualTo("uid", user)
            .get().addOnSuccessListener { docs ->
                for(doc in docs) {
                    val profileModel = doc.toObject(PosterModel::class.java)
                    if(profileModel != null)
                        Glide.with(this)
                            .load(profileModel.imageUrl)
                            .into(binding.profileImg)
                    else binding.profileImg.setImageResource(R.drawable.ic_default_profile)
                }
            }
    }

    inner class ProfileViewHolder(var binding: ListitemProfileBinding): RecyclerView.ViewHolder(binding.root)
    inner class ProfileViewAdapter(): RecyclerView.Adapter<ProfileViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val width = resources.displayMetrics.widthPixels / 3
            val view = ListitemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view.photo.layoutParams = LinearLayoutCompat.LayoutParams(width, width)

            return ProfileViewHolder(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            val posterModel = datas[position]
            Glide.with(holder.itemView.context)
                .load(posterModel.imageUrl)
                .into(holder.binding.photo)
        }
    }
}