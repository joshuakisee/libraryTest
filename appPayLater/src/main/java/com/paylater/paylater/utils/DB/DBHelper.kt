package com.paylater.paylater.utils.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONException
import org.json.JSONObject

class DBHelper (context: Context,
                factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DBHelper.DATABASE_NAME,
        factory, DBHelper.DATABASE_VERSION
    ) {

    override fun onCreate(db : SQLiteDatabase) {
        db.execSQL(
            "create table " + NOTIFICATION_TABLE_NAME +
                    "(" + NOTIFICATION_TABLE_ID + " integer primary key, " + NOTIFICATION_TITTLE + " text, " +  NOTIFICATION_STATUS + " text, " + NOTIFICATION_BODY + " text)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS  $NOTIFICATION_TABLE_NAME")
    }


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "followMeTalkPayLater.db"

        //notification db
        const val NOTIFICATION_TABLE_NAME = "notifications"
        const val NOTIFICATION_TABLE_ID = "_id"
        const val NOTIFICATION_TITTLE = "title"
        const val NOTIFICATION_STATUS = "status"
        const val NOTIFICATION_BODY = "body"

    }

    /**---------------------------------insertions-----------------------------------------------------**/
    fun insertNotifications(title:String, body:String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOTIFICATION_TITTLE, title)
        values.put(NOTIFICATION_STATUS, 1)
        values.put(NOTIFICATION_BODY, body)

        db.insert(NOTIFICATION_TABLE_NAME, null, values)
        db.close()
    }

    /**---------------------------------get data-----------------------------------------------------*/
    @SuppressLint("Range")
    fun getAllNotifications():Any{
        var notifications = ArrayList<Any>()
        var res: Cursor? = null
        val db = this.readableDatabase
        res = db.rawQuery("SELECT * FROM $NOTIFICATION_TABLE_NAME  ORDER BY $NOTIFICATION_TABLE_ID DESC", null)
        res.moveToFirst()

        while (!res.isAfterLast) {
            val map = JSONObject()
            try {
                map.put("id", res.getString(res.getColumnIndex(NOTIFICATION_TABLE_ID)))
                map.put("title", res.getString(res.getColumnIndex(NOTIFICATION_TITTLE)))
                map.put("status", res.getString(res.getColumnIndex(NOTIFICATION_STATUS)))
                map.put("body", res.getString(res.getColumnIndex(NOTIFICATION_BODY)))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            notifications.add(map)
            res.moveToNext()
        }

        return notifications
    }

    /**---------------------------------update data-----------------------------------------------------*/
    fun makeNotificationRead(
        id:String
    ){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(NOTIFICATION_STATUS, 2)

        db.update(NOTIFICATION_TABLE_NAME, values,  "$NOTIFICATION_TABLE_ID = '$id' ", null)
        db.close()
    }

    /**---------------------------------deletions-----------------------------------------------------*/
    //delete row id
    fun deleteNotification(id: String){
        val db = this.readableDatabase
        db.delete(
            NOTIFICATION_TABLE_NAME, "$NOTIFICATION_TABLE_ID = ?", arrayOf(id))
        db.close()
    }

}