package com.lpennavic.generalboard.Helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context?) : SQLiteOpenHelper(context, "db_name", null, 1) {
    // name: db파일명
    // version: db 버전 정보
    // SQLiteHelper는 writeableDatabase() 함수와 getWritableDatabase() 함수 이용

    override fun onCreate(db: SQLiteDatabase?) {
        // 앱이 설치된 후 SQLiteOpenHelper가 최초로 사용되는 순간 한 번 호출됨
        // 테이블 생성

        // USER 테이블 생성
        val userSql = """
            create table USER (
            id text primary key,
            nickname text not null,
            pw text not null,
            profile blob not null,
            intro text,
            email text unique
            )
        """
        // 쿼리문 실행
        if(db != null) db.execSQL(userSql)

        // POSTER 테이블 생성
        val posterSql = """
            create table POSTER (
            id integer primary key autoincrement,
            writer_id text not null,
            writer_nickname text not null,
            title text not null,
            content text,
            date text
            )
        """
        if(db != null) db.execSQL(posterSql)


        // COMMENT 테이블 생성
        val commantSql = """
            create table COMMANT (
            id integer primary key autoincrement,
            poster_id integer not null,
            writer_id text not null,
            content text,
            date text,
            called_user_id text
            )
        """.trimIndent()
        if(db != null) db.execSQL(commantSql)

        // COCOMENT 테이블 생성


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 데이터베이스 버전이 변경될 때마다 호출됨
        // > 부모 생성자의 마지막에 넣은 버전 번호가 db파일에 기록된 버저노다 높을 때 호출 됨
        // > 과거에 만들어진 테이블을 현재 구조가 되도록 테이블을 수정하는 작업
        // > 즉 스키마 변경
    }
}