package com.example.caparking.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.caparking.Model.Seats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Project.db";

    public DBHelper(Context context) {

        super(context, "Project.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER primary key AUTOINCREMENT, username TEXT, password TEXT, email TEXT, fullname TEXT)");
        MyDB.execSQL("create Table parkingAreas(id INTEGER primary key AUTOINCREMENT, total_seats INTEGER, perHourPrice REAL, parkingName TEXT)");
        MyDB.execSQL("create Table admin(id INTEGER primary key AUTOINCREMENT, username TEXT, password TEXT, email TEXT, fullname TEXT)");
        MyDB.execSQL("create Table seats(id INTEGER primary key AUTOINCREMENT,  departure TEXT, arrival TEXT, date TEXT, seatNumber INTEGER, seatStatus INTEGER, seatParking INTEGER, userId INTEGER, FOREIGN KEY(seatParking) REFERENCES parkingAreas(id), FOREIGN KEY(userId) REFERENCES users(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists parkingAreas");
        MyDB.execSQL("drop table if exists admin");
        MyDB.execSQL("drop table if exists seats");

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

    public Boolean insertAreas(int total_seats, Double perHourPrice, String locationName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("total_seats", total_seats);
        contentValues.put("perHourPrice", perHourPrice);
        contentValues.put("parkingName", locationName);
        long result = MyDB.insert("parkingAreas", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean updateAreas(String total_seats, Double perHourPrice, String locationName) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("total_seats", total_seats);
        contentValues.put("perHourPrice", perHourPrice);
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where locationName = ?", new String[]{locationName});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("parkingAreas", contentValues, "locationName=?", new String[]{locationName});
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
    public List<Seats> getDataFromDB(int user_id){
        List<Seats> modelList = new ArrayList<Seats>();
        String query = "select * from seats where userId="+user_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Seats model = new Seats();
                model.setDeparture(cursor.getString(0));
                model.setArrival(cursor.getString(1));
                model.setDate(cursor.getString(2));
                model.setSeatNumber(cursor.getInt(3));
                model.setParking_id(cursor.getInt(4));
                model.setUser_id(cursor.getInt(5));

                modelList.add(model);
            }while (cursor.moveToNext());
        }


        Log.d("student data", modelList.toString());


        return modelList;
    }

    public int GetId(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return id;
    }

    public int GetLocId(String locName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM parkingAreas WHERE parkingName=?", new String[]{locName});
        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return id;
    }
    public void insertSeat(SQLiteDatabase db, String departure, String arrival, String date, int seatNumber, int status, int parkingId, int userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("seatNumber",seatNumber);
        contentValues.put("seatStatus", status);
        contentValues.put("seatParking", parkingId);
        contentValues.put("userId",userId);
        db.insert("seats", null, contentValues);
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
    public Boolean checkSeats(String id, int parkingLocation) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = MyDB.rawQuery("Select * from seats where id = ? and seatStatus = ? and parkingLocation = ? ", new String[]{id, "1", String.valueOf(parkingLocation)});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("seats", contentValues, "id=?", new String[]{id});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public boolean selectSeats(SQLiteDatabase db, int parkingID, int userID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from seats where seatParking = ? and userId = ?", new String[]{String.valueOf(parkingID), String.valueOf(userID)});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    /*public Boolean checkSeatsStatus(String id, String SeatId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id = ? and seatStatus = ? ", new String[]{id, "1"});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("seats", contentValues, "id=?", new String[]{id});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }*/
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

    public boolean checkLocations(String locationname) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where parkingName = ?", new String[]{locationname});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
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