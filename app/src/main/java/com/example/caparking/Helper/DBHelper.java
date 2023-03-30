package com.example.caparking.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.caparking.Model.Card;
import com.example.caparking.Model.Seats;
import com.example.caparking.Model.User;

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
        MyDB.execSQL("create Table users(id INTEGER primary key AUTOINCREMENT, username TEXT, password TEXT," +
                " email TEXT, fullname TEXT,Phone TEXT, CredidCard TEXT ,Image BLOB)");
        MyDB.execSQL("create Table parkingAreas(id INTEGER primary key AUTOINCREMENT, total_seats INTEGER, perHourPrice REAL, parkingName TEXT)");
        MyDB.execSQL("create Table admin(id INTEGER primary key AUTOINCREMENT, username TEXT, password TEXT, email TEXT, fullname TEXT)");
        MyDB.execSQL("create Table seats(id INTEGER primary key AUTOINCREMENT,  departure TEXT, arrival TEXT, date TEXT, seatNumber INTEGER, seatStatus INTEGER, totalAmount REAL ,seatParking INTEGER, userId INTEGER, FOREIGN KEY(seatParking) REFERENCES parkingAreas(id), FOREIGN KEY(userId) REFERENCES users(id))");
        MyDB.execSQL("create Table cards(id INTEGER primary key AUTOINCREMENT, cardNumber TEXT, userId INTEGER, FOREIGN KEY(userId) REFERENCES users(id))");
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

    public Boolean insertcard(String cardNumber, int userId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardNumber", cardNumber);
        contentValues.put("userId", userId);

        long result = MyDB.insert("cards", null, contentValues);
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

    public Boolean updateAreas(int id,int total_seats, Double perHourPrice) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("total_seats", total_seats);
        contentValues.put("perHourPrice", perHourPrice);
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = MyDB.update("parkingAreas", contentValues, "id=?", new String[]{String.valueOf(id)});
            if (result == -1) return false;
            else
                return true;
        } else {
            return false;
        }
    }

    public List<Card> getCardFromDB(int userId){
        List<Card> modelList = new ArrayList<Card>();

        String query = "select cardNumber  from cards  " +
                "where userId="+userId;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Card model = new Card();
                model.setCard_number(cursor.getString(0));


                modelList.add(model);
            }while (cursor.moveToNext());
        }else{
            Log.d("card", "empty");
        }


        Log.d("data_card", modelList.toString());

        return modelList;
    }
    public List<Seats> getDataFromDB(int user_id){
        List<Seats> modelList = new ArrayList<Seats>();
        String query = "select seats.id,departure,arrival,date,seatNumber,seatStatus, totalAmount, parkingName  from seats  " +
                "INNER JOIN parkingAreas ON seats.seatParking=parkingAreas.id " +
                "where seats.userId="+user_id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Seats model = new Seats();
                model.setId(cursor.getInt(0));
                model.setDeparture(cursor.getString(1));
                model.setArrival(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setSeatNumber(cursor.getInt(4));
                model.setSeatStatus(cursor.getInt(5));
                model.setTotalAmount(cursor.getDouble(6));
                model.setParkingName(cursor.getString(7));


                modelList.add(model);
            }while (cursor.moveToNext());
        }


        Log.d("data", modelList.toString());

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
    public void insertSeat(SQLiteDatabase db, String departure, String arrival, String date, int seatNumber, int status, double totalAmount, int parkingId, int userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("seatNumber",seatNumber);
        contentValues.put("seatStatus", status);
        contentValues.put("totalAmount",totalAmount);
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
    public Boolean checkSeats(int seatParking,int seatNumber,String date) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from seats where  seatNumber=? and seatStatus = ? and seatParking = ? and date = ?", new String[]{String.valueOf(seatNumber), String.valueOf(1), String.valueOf(seatParking),date});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean selectSeats(SQLiteDatabase db, int parkingID, String date) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from seats where seatParking = ? and date = ? and seatStatus = ?", new String[]{String.valueOf(parkingID), date, String.valueOf(1)});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor viewParkingAreas(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where id= ?", new String[]{id});
        return cursor;
    }


    public Cursor selectAccount(SQLiteDatabase db,int id) {
        return db.query("users", null, " id = ? ",
                new String[]{String.valueOf(id)}, null, null, null, null);
    }
    public  void updateClientImage(SQLiteDatabase db, byte[] image, String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Image", image);
        db.update("users", contentValues, " id = ? ", new String[]{id});
    }
    public boolean checkLocations(String locationname) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from parkingAreas where parkingName = ?", new String[]{locationname});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Cursor selectImage(SQLiteDatabase db, int userID) {
        return db.query("users", new String[]{"Image"}, "id = ? ",
                new String[]{Integer.toString(userID)}, null, null,
                null, null);
    }

    public static void updateAccount(SQLiteDatabase db, String username, String fullName,
                                    String phone, String email,String creditCard, int userId) {
        ContentValues clientValues = new ContentValues();
        clientValues.put("username", HelperUtilities.capitalize(username.toLowerCase()));
        clientValues.put("fullname", HelperUtilities.capitalize(fullName.toLowerCase()));
        clientValues.put("email", email);
        clientValues.put("Phone", phone);
        clientValues.put("CredidCard", creditCard);
        db.update("users", clientValues, "id = ?", new String[]{String.valueOf(userId)});
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