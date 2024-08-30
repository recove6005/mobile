package com.lpennavic.generalboard.DAO

import android.content.Context
import android.util.Log
import com.lpennavic.generalboard.DTO.CommantDTO
import com.lpennavic.generalboard.Helper.SQLiteHelper

class CommantDAO {
    companion object {
        fun insertCommant(context: Context, commantDTO: CommantDTO) {
            val sql = """
                    insert into COMMANT (poster_id, writer_id, content, date, called_user_id)
                    values (?, ?, ?, ?, ?)
                """.trimIndent()

            val datas = arrayOf(
                commantDTO.posterId,
                commantDTO.writerId,
                commantDTO.content,
                commantDTO.date,
                commantDTO.calledUserId
            )

            val sqliteDB = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(sql, datas)
            Log.v("cmtsqlite", "commant inserted.")
            sqliteDB.close()
        }

        fun selectCommant(context: Context, commantDTO: CommantDTO): CommantDTO {
            val commant = CommantDTO(0, 0, "", "", "", "")

            return commant
        }

        fun selectAllCommant(context: Context): MutableList<CommantDTO> {
            val sql = """
                    select * from COMMANT
                """.trimIndent()
            val sqliteDB = SQLiteHelper(context).readableDatabase
            val cursor = sqliteDB.rawQuery(sql, null)
            val commantList = mutableListOf<CommantDTO>()

            while (cursor.moveToNext()) {
                val commantId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val posterId = cursor.getInt(cursor.getColumnIndexOrThrow("poster_id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val calledUserId =
                    cursor.getString(cursor.getColumnIndexOrThrow("called_user_id"))

                val commantDTO =
                    CommantDTO(commantId, posterId, writerId, content, date, calledUserId)
                commantList.add(commantDTO)
            }

            sqliteDB.close()
            cursor.close()

            return commantList
        }

        fun selectCommantByPosterId(
            context: Context,
            posterId: String
        ): MutableList<CommantDTO> {
            val sql = """
                    select * from COMMANT
                    where poster_id=?
                """.trimIndent()
            val values = arrayOf(posterId)
            val sqliteDB = SQLiteHelper(context).readableDatabase
            val cursor = sqliteDB.rawQuery(sql, values)
            val commantList = mutableListOf<CommantDTO>()

            while (cursor.moveToNext()) {
                val commantId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val posterId = cursor.getInt(cursor.getColumnIndexOrThrow("poster_id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val calledUserId =
                    cursor.getString(cursor.getColumnIndexOrThrow("called_user_id"))

                val commantDTO =
                    CommantDTO(commantId, posterId, writerId, content, date, calledUserId)
                commantList.add(commantDTO)
            }

            sqliteDB.close()
            cursor.close()

            return commantList
        }

        fun selectCommantByWriterId(
            context: Context,
            writerId: String
        ): MutableList<CommantDTO> {
            val sql = """
                    select * from COMMANT
                    where writer_id=?
                """.trimIndent()
            val values = arrayOf(writerId)
            val sqliteDB = SQLiteHelper(context).readableDatabase
            val cursor = sqliteDB.rawQuery(sql, values)
            val commantList = mutableListOf<CommantDTO>()

            while (cursor.moveToNext()) {
                val commantId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val posterId = cursor.getInt(cursor.getColumnIndexOrThrow("poster_id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val calledUserId =
                    cursor.getString(cursor.getColumnIndexOrThrow("called_user_id"))

                val commantDTO = CommantDTO(commantId, posterId, writerId, content, date, calledUserId)
                commantList.add(commantDTO)
            }
            sqliteDB.close()
            cursor.close()

            return commantList
        }

        fun updateCommant(context: Context, commantDTO: CommantDTO) {

        }


        fun deleteCommant(context: Context, commantDTO: CommantDTO) {
            val sql = """
                   delete from COMMANT
                   where id=?
               """.trimIndent()
            val datas = arrayOf(commantDTO.id)

            val sqliteDB = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(sql, datas)
            sqliteDB.close()
        }

        fun deleteCommantByPosterId(context: Context, posterId: String) {
            val sql = """
                   delete from COMMANT
                   where poster_id=?
               """.trimIndent()
            val datas = arrayOf(posterId)

            val sqliteDB = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(sql, datas)
            sqliteDB.close()
        }
    }
}
