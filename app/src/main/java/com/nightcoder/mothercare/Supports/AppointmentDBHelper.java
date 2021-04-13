package com.nightcoder.mothercare.Supports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.mothercare.Models.Appointment;

public class AppointmentDBHelper extends SQLiteOpenHelper {

    public AppointmentDBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.APPOINTMENT_TABLE
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, clientName TEXT, address TEXT, " +
                "number TEXT, need TEXT, user TEXT, vendor TEXT, status INTEGER, schedule TEXT, phototUrl TEXT, timestamp LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.APPOINTMENT_TABLE);
    }

    public boolean insertAppointment(Appointment arg) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("INSERT INTO " + Tables.APPOINTMENT_TABLE
                    + " VALUES (?,'" + arg.name + "'," +
                    "'" + arg.address + "','" + arg.number + "','" + arg.need + "','"
                    + arg.user + "','" + arg.vendor + "'," + arg.status + ",'" + arg.schedule + "','" + arg.photoUrl + "'," + arg.timestamp + ")");
            return true;
        } catch (SQLiteException e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.APPOINTMENT_TABLE
                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, clientName TEXT, address TEXT, " +
                    "number TEXT, need TEXT, user TEXT, vendor TEXT, status INTEGER, schedule TEXT, phototUrl TEXT, timestamp LONG)");
            return false;
        }
    }

    public Cursor getAppointments(String user, String vendor) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery("SELECT * FROM " + Tables.APPOINTMENT_TABLE + " WHERE user='" + user + "' AND vendor='" + vendor + "'", null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public Cursor getAppointments(String vendor) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery("SELECT * FROM " + Tables.APPOINTMENT_TABLE + " WHERE vendor='" + vendor + "'", null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
