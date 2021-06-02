package com.nightcoder.mothercare.Supports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.mothercare.Models.Feedback;

public class FeedbackDBHelper extends SQLiteOpenHelper {

    public FeedbackDBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.FEEDBACK_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, user TEXT, vendor TEXT, message TEXT, timestamp LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.FEEDBACK_TABLE);
        onCreate(db);
    }

    public boolean addFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("INSERT INTO " + Tables.FEEDBACK_TABLE + " VALUES(?,'" + feedback.user + "','" + feedback.vendor + "','" + feedback.message + "'," + feedback.timestamp + ")");
            return true;
        } catch (SQLiteException e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.FEEDBACK_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, user TEXT, vendor TEXT, message TEXT, timestamp LONG)");
            return false;
        }
    }

    public Cursor getFeedback(String vendor) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery("SELECT * FROM " + Tables.FEEDBACK_TABLE + " WHERE vendor='" + vendor + "'", null);
        } catch (SQLiteException e) {
            return null;
        }
    }
}
