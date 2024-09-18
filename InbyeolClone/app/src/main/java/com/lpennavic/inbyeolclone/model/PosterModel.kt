package com.lpennavic.inbyeolclone.model

data class PosterModel(
    var description: String = "",
    var favCnt: Long = 0,
    var favs: MutableMap<String, Boolean>? = null, // 좋아요 적용 취소 체크
    var imageUrl: String = "", //
    var timestamp: String = "",
    var uid: String = "", // 팔로잉 팔로워 관리
    var userId: String = "",
    )