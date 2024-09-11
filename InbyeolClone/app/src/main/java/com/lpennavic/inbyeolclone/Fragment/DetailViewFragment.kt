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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lpennavic.inbyeolclone.R
import com.lpennavic.inbyeolclone.databinding.FragmentDetailViewBinding
import com.lpennavic.inbyeolclone.databinding.ListitemDetailBinding
import com.lpennavic.inbyeolclone.model.PosterModel

class DetailViewFragment: Fragment() {
    lateinit var binding: FragmentDetailViewBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var curUid: String

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
        auth = FirebaseAuth.getInstance()
        curUid = auth.uid.toString()

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
            // Snapshot 은 데이터를 계속 모니터링 하기 때문에 서버의 정보가 업데이트 되는 즉시 UI에 적용됨
            firestore.collection("images").addSnapshotListener { value, error ->
                posterModels.clear()
                posterUidList.clear()
                for(item in value!!) {
                    var posterModel = item.toObject(PosterModel::class.java)
                    posterModels.add(posterModel)
                    posterUidList.add(posterModel.uid)
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
            holder.binding.favCnt.text = "Likes " + posterModel.favCnt.toString()
            holder.binding.descriptionView.text = posterModel.description

            // glider 라이브러리 사용 - 이미지 가져오기
            Glide.with(holder.itemView.context)
                .load(posterModel.imageUrl) // 실제 이미지 url
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.binding.photo) // 포스터 사진

            // 사용자가 이미 좋아요를 누른 게시물의 좋아요 버튼은 클릭된 상태로 표시
            if(posterModel.favs!!.get(curUid) == true)
                holder.binding.heartBtn.setImageResource(R.drawable.ic_heart_clicked)

            // 좋아요 클릭 이벤트
            holder.binding.heartBtn.setOnClickListener {
                favEvent(holder, position)
                holder.binding.favCnt.text = "Likes ${posterModel.favCnt}"
            }
        }

        fun favEvent(holder: DetailViewHolder, position: Int) {
            var docRefs: MutableList<DocumentReference> = mutableListOf()
            // poster의 document 주소 가져오기
            val query = firestore.collection("images")
            query.get().addOnSuccessListener { docs ->
                for (doc in docs.documents) {
                    docRefs.add(doc.reference)
                }

                // 현재 로그인 중인 사용자가 좋아요 클릭 시
                if (docRefs != null) {
                    val docRef = docRefs[position]
                    firestore.runTransaction { transaction ->

                        // 현재 포스터 스냅샷
                        val snapshot = transaction.get(docRef)
                        val posterModel = snapshot.toObject(PosterModel::class.java) ?: return@runTransaction

                        if (posterModel.favs!![curUid] == true) {
                            // 좋아요를 누른 상태
                            posterModel.favs!![curUid] = false
                            posterModel.favCnt -= 1
                            Log.v("hanilog", "fav_cnt : " + posterModel.favCnt)
                            holder.binding.heartBtn.setImageResource(R.drawable.ic_heart)
                        } else {
                            // 좋아요를 누르지 않은 상태
                            posterModel.favs!![curUid] = true
                            posterModel.favCnt += 1
                            holder.binding.heartBtn.setImageResource(R.drawable.ic_heart_clicked)
                            Log.v("hanilog", "fav_cnt : " + posterModel.favCnt)
                        }
                        transaction.set(docRef, posterModel)
                    }.addOnSuccessListener {
                        Log.e("hanilog", "transaction is successful.")
                    }.addOnFailureListener { e ->
                        Log.e("hanilog", "transaction is failed : ${e.message}")
                    }
                }




            }
        }
    }
}