package com.lpennavic.generalboard.DAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.lpennavic.generalboard.DTO.PosterDTO
import com.lpennavic.generalboard.Helper.SQLiteHelper

class PosterDAO {
    companion object {
        fun insertPoster(context: Context, posterDTO: PosterDTO) {
            val insertSql = """
                insert into POSTER (writer_id, writer_nickname, title, content, date)
                values (?, ?, ?, ?, ?)
            """.trimIndent()

            val values = arrayOf(
                posterDTO.writerId,
                posterDTO.writerNickname,
                posterDTO.title,
                posterDTO.content,
                posterDTO.date
            )

            val sqliteDB: SQLiteDatabase = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(insertSql, values)
            Log.v("sqlite", "poster inserted.")
            sqliteDB.close()
        }

        fun selectPoster(context: Context, posterId: String): PosterDTO? {
            val selectSql = """
                select * from POSTER WHERE id=?
            """.trimIndent()

            val datas = arrayOf(posterId)
            val sqliteDB: SQLiteDatabase = SQLiteHelper(context).readableDatabase
            val cursor = sqliteDB.rawQuery(selectSql, datas)

            if(cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val writerNickname = cursor.getString(cursor.getColumnIndexOrThrow("writer_nickname"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val posterDTO = PosterDTO(id, writerId, writerNickname, title, content, date)
                // posterList.add(posterDTO)
                sqliteDB.close()
                cursor.close()

                return posterDTO
            }

            sqliteDB.close()
            cursor.close()

            return null
        }

        fun selectPosterByWriter(context: Context, writerId: String): MutableList<PosterDTO> {
            val selectSql = """
                select * from POSTER WHERE writer_id=?
            """.trimIndent()

            val datas = arrayOf(writerId)
            val sqliteDB: SQLiteDatabase = SQLiteHelper(context).readableDatabase
            val cursor = sqliteDB.rawQuery(selectSql, datas)
            val posterList = mutableListOf<PosterDTO>()

            while(cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val writerNickname = cursor.getString(cursor.getColumnIndexOrThrow("writer_nickname"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val posterDTO = PosterDTO(id, writerId, writerNickname, title, content, date)

                posterList.add(posterDTO)
            }
            sqliteDB.close()
            cursor.close()

            return posterList
        }

        fun selectAllPoster(context: Context): MutableList<PosterDTO>?{
            val selectAllSql = """
                select * from POSTER
            """.trimIndent()

            val sqliteDB = SQLiteHelper(context)
            val cursor = sqliteDB.readableDatabase.rawQuery(selectAllSql, null)
            val posterList = mutableListOf<PosterDTO>()

            while(cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val writerId = cursor.getString(cursor.getColumnIndexOrThrow("writer_id"))
                val writerNickname = cursor.getString(cursor.getColumnIndexOrThrow("writer_nickname"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val posterDTO = PosterDTO(Integer.parseInt(id), writerId, writerNickname, title, content, date)
                posterList.add(posterDTO)
            }

            sqliteDB.close()
            cursor.close()

            return posterList
        }

//        fun getNewPosterId(context: Context): Int {
//            val sql = """
//                select * from POSTER ORDER BY id DESC LIMIT 1
//            """.trimIndent()
//
//            val sqliteDB = SQLiteHelper(context).readableDatabase
//            val cursor = sqliteDB.rawQuery(sql, null)
//            val newPosterId: Int
//            if(cursor.moveToNext()) {
//                newPosterId = cursor.getString(cursor.getColumnIndexOrThrow("id")).toInt() + 1
//            }
//            else newPosterId = 1
//
//            return newPosterId
//        }

        fun updatePosterTitle(context: Context, posterDTO: PosterDTO, title: String) {
            val sqliteDB = SQLiteHelper(context).writableDatabase
            val updateTitleSql = """
                update POSTER set title=? where id=?
            """.trimIndent()
            val values = arrayOf(title, posterDTO.id)
            sqliteDB.execSQL(updateTitleSql, values)
            sqliteDB.close()
        }

        fun updatePosterContent(context: Context, posterDTO: PosterDTO, content: String) {
            val sqliteDB = SQLiteHelper(context).writableDatabase
            val updateTitleSql = """
                update POSTER set content=? where id=?
            """.trimIndent()
            val values = arrayOf(content, posterDTO.id)
            sqliteDB.execSQL(updateTitleSql, values)
            sqliteDB.close()
        }

        fun deletePoster(context: Context, posterDTO: PosterDTO) {
            val deleteSql = "delete from POSTER where id=?"
            val values = arrayOf(posterDTO.id)
            val sqliteDB = SQLiteHelper(context).writableDatabase
            sqliteDB.execSQL(deleteSql, values)
            sqliteDB.close()
        }
    }
}