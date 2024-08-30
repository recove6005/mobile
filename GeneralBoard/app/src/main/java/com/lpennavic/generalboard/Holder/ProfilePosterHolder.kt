package com.lpennavic.generalboard.Holder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.DAO.PosterDAO
import com.lpennavic.generalboard.DTO.PosterDTO
import com.lpennavic.generalboard.PosterActivity
import com.lpennavic.generalboard.R

class ProfilePosterHolder(root: View): RecyclerView.ViewHolder(root) {
    val profilePosterTextTitle: TextView
    val profilePosterTextContent: TextView
    val profilePosterTextDate: TextView
    val textPosterId: TextView
    val mainWrapper: LinearLayout

    init {
        profilePosterTextTitle = root.findViewById(R.id.profileposter_text_title)
        profilePosterTextContent = root.findViewById(R.id.profileposter_text_content)
        profilePosterTextDate = root.findViewById(R.id.profileposter_text_date)
        textPosterId = root.findViewById(R.id.poster_id)
        mainWrapper = root.findViewById(R.id.listitem_profile_poster_main)
    }

    fun bind(context: Context, posterDTO: PosterDTO, userId: String) {
        val poster = PosterDAO.selectPoster(context, posterDTO.id.toString())
        if(poster!=null) {
            profilePosterTextTitle.text = poster.title
            profilePosterTextContent.text = poster.content
            profilePosterTextDate.text = poster.date
            textPosterId.text = poster.id.toString()
        }

        mainWrapper.setOnClickListener {
            val intent = Intent(context, PosterActivity::class.java)
            val ctx = context as Activity
            intent.putExtra("poster_id", posterDTO.id.toString())
            intent.putExtra("user_id", userId)
            intent.putExtra("fragment_id", R.id.nav_profile)
            ctx.startActivity(intent)
            ctx.finish()
        }

    }
}