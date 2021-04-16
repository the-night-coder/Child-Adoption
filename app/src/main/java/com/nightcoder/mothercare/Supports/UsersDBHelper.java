package com.nightcoder.mothercare.Supports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.mothercare.Models.User;

public class UsersDBHelper extends SQLiteOpenHelper {

    public UsersDBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USERS + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, userType INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USER_DETAILS + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, fullName TEXT, photoUrl TEXT, userType INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USERS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USER_DETAILS);
        onCreate(db);
    }

    public User getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Tables.USER_DETAILS + " WHERE email='" + email + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                User user = new User();
                user.email = cursor.getString(cursor.getColumnIndex("email"));
                user.fullName = cursor.getString(cursor.getColumnIndex("fullName"));
                user.photoUrl = cursor.getString(cursor.getColumnIndex("photoUrl"));
                user.password = cursor.getString(cursor.getColumnIndex("password"));
                user.userType = cursor.getInt(cursor.getColumnIndex("userType"));
                cursor.close();
                return user;
            } else {
                return null;
            }

        } catch (SQLiteException e) {
            return null;
        }
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("INSERT OR REPLACE INTO " + Tables.USERS + " VALUES('" + user.email + "','" + user.password + "'," + user.userType + ")");
            return true;
        } catch (SQLiteException e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USERS + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, userType INTEGER)");
            return false;
        }
    }

    public boolean insertUserDetails(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("INSERT OR REPLACE INTO " + Tables.USER_DETAILS + " VALUES('" + user.email + "','" + user.password + "','" + user.fullName + "','" + user.photoUrl + "'," + user.userType + ")");
            return true;
        } catch (SQLiteException e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USER_DETAILS + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, fullName TEXT, photoUrl TEXT, userType INTEGER)");
            return false;
        }
    }


    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Tables.USERS + " WHERE email='" + email + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                User user = new User();
                user.email = cursor.getString(cursor.getColumnIndex("email"));
                user.password = cursor.getString(cursor.getColumnIndex("password"));
                user.userType = cursor.getInt(cursor.getColumnIndex("userType"));
                cursor.close();
                return user;
            } else {
                return null;
            }
        } catch (SQLiteException e) {
            return null;
        }
    }
}
