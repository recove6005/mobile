package com.lpennavic.inbyeolclone.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.lpennavic.inbyeolclone.R
import com.lpennavic.inbyeolclone.databinding.FragmentDetailViewBinding
import com.lpennavic.inbyeolclone.databinding.ListitemDetailBinding
import com.lpennavic.inbyeolclone.model.PosterModel

class DetailViewFragment: Fragment() {
    lateinit var binding: FragmentDetailViewBinding
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_view, container, false)
        firestore = FirebaseFirestore.getInstance()

        // 리사이클러뷰 어뎁터 설정
        binding.photoList.adapter = DetailViewRecyclerView()
        binding.photoList.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    inner class DetailViewHolder(var binding: ListitemDetailBinding): RecyclerView.ViewHolder(binding.root)
    inner class DetailViewRecyclerView: RecyclerView.Adapter<DetailViewHolder>() {
        var posterModels = arrayListOf<PosterModel>()
        var posterUidList = arrayListOf<String>()
        init {
            firestore.collection("images").addSnapshotListener { value, error ->
                posterModels.clear()
                posterUidList.clear()
                for(item in value!!) {
                    var posterModel = item.toObject(PosterModel::class.java)
                    posterModels.add(posterModel)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
            // Recyclerview의 행 하나에 어떤 xml을 표시할 지 설정
            val view = ListitemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DetailViewHolder(view)
        }

        override fun getItemCount(): Int {
            // 보여줄 행의 개수 설정
            return posterModels.size
        }

        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            // 데이터 바인딩
            val posterModel = posterModels.get(position)

            holder.binding.userId.text = posterModel.userId
            holder.binding.favCnt.text = "Liks " + posterModel.favCnt.toString()
            holder.binding.descriptionView.text = posterModel.description

            // glider 라이브러리 사용 - 이미지 가져오기
            Glide.with(holder.itemView.context)
                .load(posterModel.imageUrl) // 실제 이미지 url
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.binding.photo) // 포스터 사진

        }

    }
}