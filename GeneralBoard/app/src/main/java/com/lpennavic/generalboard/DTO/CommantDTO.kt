package com.lpennavic.generalboard.DTO

class CommantDTO(
    val id: Int,
    val posterId: Int,
    val writerId: String,
    val content: String,
    val date: String,
    val calledUserId: String
)