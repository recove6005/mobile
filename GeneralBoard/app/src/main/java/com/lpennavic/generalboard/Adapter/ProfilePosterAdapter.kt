package com.lpennavic.generalboard.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.DTO.PosterDTO
import com.lpennavic.generalboard.Holder.ProfilePosterHolder
import com.lpennavic.generalboard.R

class ProfilePosterAdapter(val context: Context,
                           val userId: String,
                           val datas: MutableList<PosterDTO>
)
    : RecyclerView.Adapter<ProfilePosterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePosterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.listitem_profile_poster, parent, false)
        return ProfilePosterHolder(view)
    }

    override fun onBindViewHolder(holder: ProfilePosterHolder, position: Int) {
        holder.bind(context, datas[position], userId)
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}