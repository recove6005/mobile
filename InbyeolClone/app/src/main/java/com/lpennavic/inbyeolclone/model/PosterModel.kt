package com.lpennavic.inbyeolclone.model

data class PosterModel(
    var description: String? = null,
    var imageUrl: String? = null, //
    var uid: String? = null, // 팔로잉 팔로워 관리
    var userId: String? = null,
    var timestamp:Long? = null,
    var favCnt: Int? = 0,
    var favs: MutableMap<String, Boolean> = HashMap() // 좋아요 적용 취소 체크
    )