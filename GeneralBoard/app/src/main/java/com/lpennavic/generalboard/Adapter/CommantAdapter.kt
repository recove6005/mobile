package com.lpennavic.generalboard.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.DTO.CommantDTO
import com.lpennavic.generalboard.Holder.CommantHolder
import com.lpennavic.generalboard.PosterActivity
import com.lpennavic.generalboard.R

class CommantAdapter(
    val context: Context,
    val userId: String,
    val datas:MutableList<CommantDTO>,
    private val listener: PosterActivity
)
    : RecyclerView.Adapter<CommantHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.listitem_commant, parent, false)
        return CommantHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CommantHolder, position: Int) {
        // 뷰와 데이터 바인딩
        holder.bind(context, datas[position], userId)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < datas.size) {
            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemChanged(position, datas.size)
        }



    }
}

// ListView 커스텀어뎁터
//class CommantAdapter(context: Context, val resId: Int, val datas: MutableList<CommantDTO>, val userId: String)
//    : ArrayAdapter<CommantDTO>(context, resId) {
//    override fun getCount(): Int {
//        return datas.size
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var convertView = convertView
//        if(convertView == null) {
//            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//                    as LayoutInflater
//            convertView = inflater.inflate(resId, null)
//            val holder = CommantHolder(convertView)
//            convertView!!.tag = holder
//        }
//
//        val holder = convertView.getTag() as CommantHolder
//        val profileImgView = holder.commantImgProfile
//        val nicknameView = holder.commantTextNickname
//        val content = holder.commantTextContent
//        val date = holder.commantTextDate
//        val deleteBtn = holder.commantBtnDeletebtn
//
//        val writer = UserDAO.selectUser(context, datas[position].writerId)
//        val user = UserDAO.selectUser(context, userId)
//
//        if(writer != null) {
//            // 각 뷰와 내용 바인딩
//            val profileImg = BitmapFactory.decodeFile(writer.profile.toString())
//            profileImgView.setImageBitmap(profileImg)
//            nicknameView.text = writer.nickname
//            content.text = datas[position].content
//            date.text = datas[position].date
//
//            // 댓글 삭제 버튼
//            // 댓글 작성자와 로그인 중인 사용자가 같으면 삭제버튼을 보여주고 아니면 숨김
//            if(user?.id.contentEquals(writer.id)) deleteBtn.visibility = View.VISIBLE
//            else deleteBtn.visibility = View.GONE
//            deleteBtn.setOnClickListener {
//                // 댓글 삭제
//                CommantDAO.deleteCommant(context, datas[position])
//                notifyDataSetChanged()
//            }
//        }
//        return convertView
//    }
//}