package com.lpennavic.generalboard.DTO

import java.io.File

class UserDTO(
    val id: String,
    val nickname: String,
    val pw: String,
    val profile: File?,
    val intro: String,
    val email: String
)
// 코틀린은 객체의 프로퍼티에 직접 접근 > getter setter와 동일하게 동작함


// 기본 생성자와 프로퍼티 초기화 방식
//class UserDTO {
    //    val id: String = ""
    //    val nickname: String = ""
    //    val pw: String = ""
    //    val profile: File? = null
    //    val intro: String = ""
    //    val email: String = ""

    // >> 다음과 같이 userDTO객체 생성
    //    val userDTO = UserDTO().apply {
    //        id = "123"
    //        nickname = "john_doe"
    //        pw = "password123"
    //        profile = File("path/to/file")
    //        intro = "Hello!"
    //        email = "john@example.com"
    //    }
//}