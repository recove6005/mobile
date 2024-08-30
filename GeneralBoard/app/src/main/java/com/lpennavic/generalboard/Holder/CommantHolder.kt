package com.lpennavic.generalboard.Holder

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.DAO.CommantDAO
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.DTO.CommantDTO
import com.lpennavic.generalboard.Interface.OnCommantItemClickListener
import com.lpennavic.generalboard.R

class CommantHolder(root: View, val listener:OnCommantItemClickListener): RecyclerView.ViewHolder(root){
    val commantImgProfile: ImageView
    val commantTextNickname: TextView
    val commantTextContent: TextView
    val commantBtnDeletebtn: ImageButton
    val commantTextDate: TextView

    init {
        commantImgProfile = root.findViewById(R.id.commant_img_profile)
        commantTextNickname = root.findViewById(R.id.commant_text_nickname)
        commantTextContent = root.findViewById(R.id.commant_text_content)
        commantBtnDeletebtn = root.findViewById(R.id.commant_btn_deleteBtn)
        commantTextDate = root.findViewById(R.id.commant_text_date)
    }

    fun bind(context: Context, commant: CommantDTO, userId: String) {
        val writer = UserDAO.selectUser(context, commant.writerId)
        val user = UserDAO.selectUser(context, userId)

        if(writer!=null && user!=null) {
            // val profileBitmap = BitmapFactory.decodeFile(writer.profile.toString())
            // writer.profile.toString()를 바로 decodeFile에 전달했더니 이미지가 안 뜸
            val writerImgFile = writer.profile.toString()
            val profileBitmap = BitmapFactory.decodeFile(writerImgFile)
            if(profileBitmap != null) commantImgProfile.setImageBitmap(profileBitmap)
            else commantImgProfile.setImageResource(R.drawable.profile_5050)

            commantTextNickname.text = writer.nickname

            // 하나의 TextView에 calledUserId 부분과 content 부분의 문자열 색깔을 다르게 적용하기
            // commantTextContent.text = commant.calledUserId + " " + commant.content
            // SpannableString 생성
            // val spannable = SpannableString("${commant.calledUserId} ${commant.content}")
            // 각 문자열 부분의 색상 정의
            // val parsedColor = Color.parseColor("#D4F4FA")
            // val userIdStart = 0
            // val userIdEnd = commant.calledUserId.length
            // spannable.setSpan(ForegroundColorSpan(parsedColor), userIdStart, userIdEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            commantTextContent.text = commant.calledUserId + " " + commant.content

            commantTextDate.text = commant.date

            commantBtnDeletebtn.visibility = if(user.id==writer.id) View.VISIBLE else View.GONE

            commantBtnDeletebtn.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(context)
                    .setMessage("Would like to delete this comment?")
                    .setPositiveButton("OK") {
                        dialog, which ->
                        CommantDAO.deleteCommant(context, commant)
                        dialog.dismiss()
                        if(adapterPosition != RecyclerView.NO_POSITION) listener.onDeleteBtnClick(adapterPosition)
                    }
                    .setNegativeButton("NO") {
                        dialog, which ->
                        dialog.dismiss()
                    }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }

    }

}