package com.lpennavic.generalboard.DAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.lpennavic.generalboard.Helper.SQLiteHelper
import com.lpennavic.generalboard.DTO.UserDTO
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.arrayOf as arrayOf

class UserDAO {
    companion object {
        fun insertUser(context: Context, userDTO: UserDTO) {
            // 회원가입
            val sql = """
                insert into USER (id, nickname, pw, profile, intro, email) 
                values(?,?,?,?,?,?)
            """

            val userData = arrayOf(
                userDTO.id,
                userDTO.nickname,
                userDTO.pw,
                userDTO.profile,
                userDTO.intro,
                userDTO.email
            )

            val sqliteDB:SQLiteDatabase = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(sql, userData)
            Log.v("sqlite", "user inserted.")
            sqliteDB.close()
        }

        fun selectUser(context: Context, id: String) : UserDTO? {
            // 쿼리문
            val sql = "select * from USER where id=?"
            // ?에 대체될 값(바인드 변수)
            val userId = arrayOf("$id")

            // DB열기
            val sqliteHelper = SQLiteHelper(context)

            // 쿼리 실행
            // rawQuery() 함수는 sql쿼리 실행 후 결과를 Cursor 객체로 반환함
            val cursor = sqliteHelper.writableDatabase.rawQuery(sql,userId)
            // 커서 이동
            // Cursor 객체 : DB 쿼리 결과는 여러 개일 수 있으며, 결과의 행을 순차적으로 탐색하는 객체
            // moveToNext()메서드 : 커서를 다음 행으로 이동 시키고, 다음 행이 있으면 true를 반환, 없으면 false 반환
            // 결과 행으로 이동한 후 데이터에 접근
            // >> Cursor객체를 생성하면 기본적으로 전체 결과 집합의 첫 번째 행 전에 위치함
            // >> 따라서 기본적으로 moveToNext() 메서드를 호출하여 커서를 실제 데이터가 있는 행으로 이동시켜야 함
            // >> Cursor가 현재 행에 있는 데이터는 getClumnIndex() 와 같은 메서드를 통해 접근
            if(cursor.moveToNext()) {
                // 결과 데이터가 있으면 다음의 과정 실행, 컬럼 이름을 지정하여 컬럼의 순서값을 가져옴
                // DB의 인덱스 값을 먼저 가져온 후 getColumnIndex(),
                // getString()메서드에 인덱스 값을 전달해서 DB의 데이터 값을 가져옴
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val nickname = cursor.getString(cursor.getColumnIndexOrThrow("nickname"))
                val pw = cursor.getString(cursor.getColumnIndexOrThrow("pw"))

                // File 값 가져오기
                val profileIndex = cursor.getColumnIndex("profile") // 커서에서 현재 결과값 행의 인덱스 가져오기
                val profilePath = cursor.getString(profileIndex) // 파일 경로를 문자열로 읽어옴
                val profile = File(profilePath) // 파일 경로를 File 객체에 담음

                val intro = cursor.getString(cursor.getColumnIndexOrThrow("intro"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                val userDTO = UserDTO(id, nickname, pw, profile, intro, email)
                sqliteHelper.close()
                cursor.close()

                return userDTO
            }

            // 결과값이 없을 경우 null 값 반환
            sqliteHelper.close()
            cursor.close()

            return null
        }

        fun selectAllUser(context: Context): MutableList<UserDTO> {
            val sql = """
                select * from USER
            """
            val sqliteHelper = SQLiteHelper(context)
            val cursor = sqliteHelper.writableDatabase.rawQuery(sql, null)

            val userList = mutableListOf<UserDTO>()

            while(cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val nickname = cursor.getString(cursor.getColumnIndexOrThrow("nickname"))
                val pw = cursor.getString(cursor.getColumnIndexOrThrow("pw"))

                val profileIndex = cursor.getColumnIndex("profile") // 커서에서 현재 결과값 행의 인덱스 가져오기
                val profilePath = cursor.getString(profileIndex) // 파일 경로를 문자열로 읽어옴
                val profile = File(profilePath) // 파일 경로를 File 객체에 담음

                val intro = cursor.getString(cursor.getColumnIndexOrThrow("intro"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                val userDTO = UserDTO(id, nickname, pw, profile, intro, email)
                userList.add(userDTO)
            }

            sqliteHelper.close()
            cursor.close()

            return userList
        }

        fun updateUser(context: Context, updateType: Int, obj: Any, user: UserDTO) {
            // updateType : 1 >> nickname
            // updateType : 2 >> pw
            // updateType : 3 >> profile
            // updateType : 4 >> intro
            // updateType : 5 >> email (인증 과정 거치기)
            val sqLiteHelper = SQLiteHelper(context)
            var sql = ""

            // 1) args를 MutableList 로 초기화
            // var datas = mutableListOf<Any?>()
            // execSQL() 메서드는 SQLite DB에서 SQL쿼리를 실행하는 데 사용되며, 매개변수를 배열로 받음
            // MutableList 타입은 배열이 아니기 때문에 호환되지 않음
            // >> MutableList<>를 Array<>로 변환한 후 전달

            // 2) args를 Array로 초기화
            // var datas = Array<Any?>(2) {null}
            var datas = arrayOfNulls<Any>(2)

            when(updateType) {
                1 -> {
                    // nickname 변경
                    sql = """
                    update USER
                    set nickname=?
                    where id=?
                """.trimIndent()
                    // datas.add(obj.toString())
                    // datas.add(user.id)

                    datas[0] = obj.toString()
                    datas[1] = user.id
                }
                2 -> {
                    // pw 변경
                    sql = """
                        update USER
                        set pw=?
                        where id=?
                    """.trimIndent()
                    // datas.add(obj.toString())
                    // datas.add(user.id)

                    datas[0] = obj.toString()
                    datas[1] = user.id
                }
                3 -> {
                    // profile 변경
                    sql = """
                        update USER
                        set profile=?
                        where id=?
                    """.trimIndent()

                    val bitmap = obj as Bitmap
                    val profileImgFile = File(context.filesDir, "profileBitmap")
                    val outputStream = FileOutputStream(profileImgFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    datas[0] = profileImgFile
                    datas[1] = user.id
                }
                4 -> {
                    // intro 변경
                    sql = """
                        update USER
                        set intro=?
                        where id=?
                    """.trimIndent()
                    // datas.add(obj.toString())
                    // datas.add(user.id)

                    datas[0] = obj.toString()
                    datas[1] = user.id
                }
                5 -> {
                    // email 변경
                    sql = """
                        update USER
                        set email=?
                        where id=?
                    """.trimIndent()
                    // datas.add(obj.toString())
                    // datas.add(user.id)

                    datas[0] = obj.toString()
                    datas[1] = user.id
                }
            }

            sqLiteHelper.writableDatabase.execSQL(sql, datas)
            sqLiteHelper.close()
        }

        fun deleteUser(context: Context, user: UserDTO) {
            // 회원탈퇴
            val sql = "delete from USER where id=?"
            val args = arrayOf(user.id)
            val sqliteHelper = SQLiteHelper(context)
            sqliteHelper.writableDatabase.execSQL(sql, args)
            sqliteHelper.close()
        }
    }
}