package com.michtech.chatroom.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private static final String DatabaseName = "ChatRoom";
    private static final int Version = 1;
    private static final String TableChats = "Chats";
    private static final String Messages = "Messages";
    private static final String DDate = "DDate";
    private static final String TableUser = "User";
    private static final String UserName = "UserName";
    private static final String Email = "Email";
    private static final String Type = "Type";
    private static final String FFrom = "FFrom";

    public DatabaseManager(Context context){
        super(context, DatabaseName, null, Version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //
    }
    public void CheckTables(){
        db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TableUser, null, null, null, null, null, null);
        }catch (Exception e){
            CreateTableUser();
        }
        try{
            cursor = db.query(TableChats, null, null, null, null, null, null);
        }catch (Exception e){
            CreateTableMessages();
        }
        db.close();
    }
    /*
    Table User
     */
    public boolean CheckUser(){
        db = this.getReadableDatabase();
        boolean a =false;
        String query = "SELECT COUNT(*) FROM "+TableUser;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            if(cursor.getInt(0)>0){
                a = true;
            }
        }
        db.close();
        return a;
    }
    public void addUser(String userName, String email){
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserName, userName);
        values.put(Email, email);
        db.insert(TableUser, null, values);
        db.close();
    }
    public String GetUserName(){
        db = this.getReadableDatabase();
        String userName = "";
        String query = "SELECT "+UserName+" FROM "+TableUser;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            userName = cursor.getString(0);
        }
        return userName;
    }
    /*
    Table Messages
     */
    public long saveMessage(String userName, String message, int type, String date){
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Messages, message);
        values.put(FFrom, userName);
        values.put(Type, type);
        values.put(DDate, date);

        return db.insert(TableChats, null, values);
    }
    public List<DbHelper> getMessage(){
        db = this.getReadableDatabase();
        List<DbHelper> dbHelperList = new ArrayList<>();
        String query = "SELECT * FROM "+TableChats;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                DbHelper dbHelper = new DbHelper();
                dbHelper.setId(cursor.getInt(0));
                dbHelper.setMessage(cursor.getString(1));
                dbHelper.setFFrom(cursor.getString(2));
                dbHelper.setType(cursor.getInt(3));
                dbHelper.setDDate(cursor.getString(4));

                dbHelperList.add(dbHelper);

            }while(cursor.moveToNext());
        }
        return dbHelperList;
    }
    public String getLastMessage(){
        db = this.getReadableDatabase();
        String message="none";
        String query = "SELECT "+Messages+" FROM "+TableChats+" ORDER BY id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            message = cursor.getString(0);
        }
        return message;
    }
    /*
    Create Tables
     */
    private void CreateTableUser(){
        db = this.getReadableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS "+ TableUser +"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                UserName+" TEXT NOT NULL, "+
                Email+" TEXT NOT NULL);";
        db.execSQL(query);
        db.close();
    }
    private void CreateTableMessages(){
        db = this.getReadableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS "+ TableChats +"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Messages+" TEXT NOT NULL, "+
                FFrom+" TEXT NOT NULL, "+
                Type+" INTEGER NOT NULL, "+
                DDate+" TEXT NOT NULL);";
        db.execSQL(query);
        db.close();
    }
}
