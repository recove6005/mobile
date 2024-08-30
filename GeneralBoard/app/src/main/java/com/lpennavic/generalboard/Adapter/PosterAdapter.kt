package com.lpennavic.generalboard.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.lpennavic.generalboard.DTO.PosterDTO
import com.lpennavic.generalboard.Holder.PosterHolder

class PosterAdapter(context: Context, val resId: Int, val datas: MutableList<PosterDTO>)
    : ArrayAdapter<PosterDTO>(context, resId) {
    override fun getCount(): Int {
        return datas.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            convertView = inflater.inflate(resId, null)
            val holder = PosterHolder(convertView)
            convertView!!.tag = holder
        }

        val holder = convertView.getTag() as PosterHolder
        val titleView = holder.title
        val contentView = holder.content
        val dateView = holder.date
        val posterIdView = holder.posterId

        val title = datas[position].title
        val content = datas[position].content
        val date = datas[position].date
        val posterId = datas[position].id

        titleView.text = title
        contentView.text = content
        dateView.text = date
        posterIdView.text = posterId.toString()

        return convertView
    }
}