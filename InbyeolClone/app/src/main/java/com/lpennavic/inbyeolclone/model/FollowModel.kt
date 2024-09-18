package com.lpennavic.inbyeolclone.model

data class FollowModel (
    var followers: MutableMap<String, Boolean> = HashMap(),
    var followerCnt: Long = 0,
    var followings: MutableMap<String, Boolean> = HashMap(),
    var followingCnt: Long = 0,
    )