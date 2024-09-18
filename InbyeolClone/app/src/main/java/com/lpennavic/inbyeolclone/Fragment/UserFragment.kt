package com.lpennavic.inbyeolclone.Fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.R
import com.lpennavic.inbyeolclone.databinding.FragmentUserBinding
import com.lpennavic.inbyeolclone.databinding.ListitemProfileBinding
import com.lpennavic.inbyeolclone.model.PosterModel
import com.lpennavic.inbyeolclone.model.ProfileModel

class UserFragment: Fragment() {
    lateinit var binding: FragmentUserBinding
    lateinit var user: String
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    lateinit var profileViewAdapter: ProfileViewAdapter

    var datas: ArrayList<PosterModel> = ArrayList()

    val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data.let { uri ->
                if(user != null) {
                    var profileModel = ProfileModel(user, uri.toString())
                    firestore.collection("profiles").document(user).set(profileModel)
                    profileViewAdapter.notifyDataSetChanged()
                    getProfileImg()
                } else {
                    Log.d("hanilog", "user data is null.")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // 프로필 대상 구분
        user = auth.currentUser!!.uid

        // 프로필 대상 데이터 가져오기
        profileViewAdapter = ProfileViewAdapter()
        firestore.collection("images").whereEqualTo("uid", user)
            .get().addOnSuccessListener { docs ->
                for (doc in docs) {
                    val posterModel = doc.toObject(PosterModel::class.java)
                    datas.add(posterModel)
                }
                binding.photoList.adapter = profileViewAdapter
                binding.photoList.layoutManager = GridLayoutManager(context, 3)

                // 포스팅 개수
                binding.postCnt.text = "POST\n${datas.size}"
            }
            .addOnFailureListener{ e ->
                Log.w("hanilog", "Error getting documents: ", e)
            }

        // 프로필 이미지 표시
        getProfileImg()

        // 프로필 이미지 변경 이벤트
        binding.profileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            launcher.launch(intent)

            // 변경된 프로필 이미지 표시
            getProfileImg()
        }

        return binding.root
    }

    fun getProfileImg() {
        Log.d("hanilog", "profile button is clicked")
        // 프로필 대상 프로필 사진 가져오기
        firestore.collection("profiles").whereEqualTo("uid", user)
            .get().addOnSuccessListener { docs ->
                    val doc = docs.documents[0]
                    val profileModel = doc.toObject(PosterModel::class.java)
                    if(profileModel != null)
                        Glide.with(this)
                            .load(profileModel.imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.profileImg)
                    else binding.profileImg.setImageResource(R.drawable.ic_default_profile)

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