package com.example.caparking.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Context
    Context _context;

    public static final String PARKING_USER = "PARKING_USER";
    public static final String TOKEN = "token";
    public static final String PARKING_ID = "parking_id";
    public static final String PARKING_NAME = "parking_name";
    public static final String PARKING_NUMBER = "parking_number";
    public static final String PARKING_TIME = "parking_time";
    public static final String IS_LOGIN = "isLoggedIn";
    public static final String PARKING_PRICE = "parking_price";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PARKING_USER, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(TOKEN, id);

        // commit changes
        editor.commit();
    }

    public void createParkingSession(int id){

        editor.putInt(PARKING_ID, id);

        // commit changes
        editor.commit();
    }

    public void createSeatSession(String name,int number,String time, String price){

        editor.putInt(PARKING_NUMBER, number);
        editor.putString(PARKING_TIME,time);
        editor.putString(PARKING_NAME,name);
        editor.putString(PARKING_PRICE,price);

        // commit changes
        editor.commit();
    }

    /*public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Starting Login Activity
            _context.startActivity(i);
        }

    }*/

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public int getToken(){
        return pref.getInt(TOKEN, 0);
    }

    public int getParkingId(){
        return pref.getInt(PARKING_ID, 0);
    }

    public String getName(){
        return pref.getString(PARKING_NAME, "");
    }

    public String getTime(){
        return pref.getString(PARKING_TIME, "");
    }

    public int getNumber(){return pref.getInt(PARKING_NUMBER, 0);}

    public String getPrice(){
        return pref.getString(PARKING_PRICE, "");
    }

    public void logOut(){
        editor.clear().commit();
    }

}
