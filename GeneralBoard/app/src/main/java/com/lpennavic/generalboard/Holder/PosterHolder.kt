package com.lpennavic.generalboard.Holder

import android.view.View
import android.widget.TextView
import com.lpennavic.generalboard.R

class PosterHolder(root: View) {
    var title: TextView
    var content: TextView
    var date: TextView
    var posterId: TextView

    init {
        title = root.findViewById(R.id.listitem_text_title)
        content = root.findViewById(R.id.listitem_text_content)
        date = root.findViewById(R.id.listitem_text_date)
        posterId = root.findViewById(R.id.poster_id)
    }
}