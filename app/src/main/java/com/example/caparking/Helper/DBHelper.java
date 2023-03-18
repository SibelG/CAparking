package com.example.caparking.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Project.db";

    public DBHelper(Context context) {

        super(context, "Project.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, email TEXT, fullname TEXT)");
        MyDB.execSQL("create Table parkingAreas(id INTEGER primary key AUTOINCREMENT, departure TEXT, arrival TEXT, date TEXT, total_seats TEXT, perHourPrice REAL, parkingLocation TEXT, FOREIGN KEY(parkingLocation) REFERENCES parkingLocations(id))");
        MyDB.execSQL("create Table admin(username TEXT primary key, password TEXT, email TEXT, fullname TEXT)");
        MyDB.execSQL("create Table seats(id INTEGER primary key AUTOINCREMENT, seatNumber INTEGER, seatStatus TEXT, seatParking TEXT, FOREIGN KEY(seatParking) REFERENCES parkingAreas(id))");
        MyDB.execSQL("create TABLE parkingLocations (id INTEGER PRIMARY KEY AUTOINCREMENT , parkingName TEXT COLLATE NOCASE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists parkingAreas");
        MyDB.execSQL("drop table if exists admin");
        MyDB.execSQL("drop table if exists seats");
        MyDB.execSQL("drop table if exists parkingLocations");
    }

    public boolean insertParkingLocation(String parkingLocationName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues parkingValues = new ContentValues();
        parkingValues.put("parkingName", parkingLocationName);
        long result = MyDB.insert("parkingLocations", null, parkingValues);
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean insertData(String fullname, String email, String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }


    public Boolean insertadmin(String fullname, String email, String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("admin", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkadminusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from admin where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkuseremailpassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkadminusernamepassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from admin where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean insertAreas(String id, String departure, String arrival, String date, String total_seats, Double fare, int locationId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("total_seats", total_seats);
        contentValues.put("fare", fare);
        contentValues.put("parkingLocation", locationId);
        long result = MyDB.insert("parkingAreas", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean updateAreas(String id, String departure, String arrival, String date, String total_seats, Double fare, int locationId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("total_seats", total_seats);
        contentValues.put("fare", fare);
        contentValues.put("parkingLocation", locationId);
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("parkingAreas", contentValues, "id=?", new String[]{id});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public Boolean deleteParkingAreas(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = MyDB.delete("parkingAreas", "id=?", new String[]{id});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public void insertSeat(SQLiteDatabase db, String status, int parkingId) {
        ContentValues seatValues = new ContentValues();
        seatValues.put("seatStatus", status);
        seatValues.put("seatParking", parkingId);
        db.insert("seats", null, seatValues);
    }
    public Boolean updateSeat(String id, String seatStatus) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("seatStatus", seatStatus);
        Cursor cursor = MyDB.rawQuery("Select * from seats where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("seats", contentValues, "id=?", new String[]{id});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }
    public Cursor viewParkingAreas() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas", null);
        return cursor;
    }

    /*public boolean checkSeatsStatus(String status) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from seats where seatStatus = ?", new String[]{status});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }*/


    /*public Cursor getListContents(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor data = MyDB.rawQuery("Select * from buses", null);
        return data;*/
    public Cursor selectAccount(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.query("users", null, " email = ? ",
                new String[]{email}, null, null, null, null);
    }

    public Cursor selectLocationName(String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.query("parkingLocations", null, " parkingName = ? ",
                new String[]{name}, null, null, null, null);
    }

    public boolean checkid(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id = ?", new String[]{id});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean checkArea(String seatNumber) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from seats where seatNumber = ?", new String[]{seatNumber});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}