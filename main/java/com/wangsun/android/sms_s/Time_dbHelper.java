package com.wangsun.android.sms_s;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by WANGSUN on 02-Jan-17.
 */

public class Time_dbHelper extends SQLiteOpenHelper {

    public static final String DataBaseName="SMSs.DB";
    public static final int DataBase_version=1;
    public static final String Create_query=
            "CREATE TABLE IF NOT EXISTS "+ Time_contractor.userInfo.TABLE+"("+
                    Time_contractor.userInfo.Id_+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Time_contractor.userInfo.Msg+" TEXT,"+
                    Time_contractor.userInfo.Num+" TEXT,"+
                    Time_contractor.userInfo.Date_+" INTEGER,"+
                    //Time_contractor.userInfo.Time+" INTEGER,"+
                    Time_contractor.userInfo.Status+" TEXT);";

    public Time_dbHelper(Context context) {
        super(context, DataBaseName, null, DataBase_version);
        //Log.e("DB oparation","Database created....");
    }

    public void create_table(SQLiteDatabase db){
        db.execSQL(Create_query);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(Create_query);
        //Log.e("DB oparation","table created...");
    }

    public void addvalues(String msg, String num, long date, String status, SQLiteDatabase db){

        ContentValues contentValues=new ContentValues();

        //contentValues.put(Time_contractor.userInfo.Id_,email);
        contentValues.put(Time_contractor.userInfo.Msg,msg);
        contentValues.put(Time_contractor.userInfo.Num,num);
        contentValues.put(Time_contractor.userInfo.Date_,date);
        contentValues.put(Time_contractor.userInfo.Status,status);

        db.insert(Time_contractor.userInfo.TABLE,null,contentValues);
    }

    public Cursor getInformation(SQLiteDatabase db){
        Cursor cursor;
        String[] projection= {
                Time_contractor.userInfo.Id_,
                Time_contractor.userInfo.Msg,
                Time_contractor.userInfo.Num,
                Time_contractor.userInfo.Date_,
                Time_contractor.userInfo.Status
        };

        cursor=db.query(Time_contractor.userInfo.TABLE,projection,null,null,null,null,null);
        return cursor;
    }

    public int updateValues(int id,String msg, String num, long date, String status, SQLiteDatabase db){
        ContentValues contentValues=new ContentValues();


        contentValues.put(Time_contractor.userInfo.Msg,msg);
        contentValues.put(Time_contractor.userInfo.Num,num);
        contentValues.put(Time_contractor.userInfo.Date_,date);
        contentValues.put(Time_contractor.userInfo.Status,status);

        //contentValues.put(contractor_notice.userInfo.PASSWORD,password);
        /*String selection = Time_contractor.userInfo.Id_ + " LIKE?";
        int[] id_ = {id};
        int count=db.update(Time_contractor.userInfo.TABLE,contentValues,selection,id_);*/

        int count = db.update(Time_contractor.userInfo.TABLE,contentValues,
                Time_contractor.userInfo.Id_+"="+id, null); //"id="+id

        return count;
    }

    public int updateStatus(int id, String status, SQLiteDatabase db){
        ContentValues contentValues=new ContentValues();

        contentValues.put(Time_contractor.userInfo.Status,status);

        int count = db.update(Time_contractor.userInfo.TABLE,contentValues,
                Time_contractor.userInfo.Id_+"="+id, null);

        return count;
    }

    public Cursor getId(SQLiteDatabase db) { //to get the largest id and compare with firebase database
        Cursor cursor;
        String[] projection = {Time_contractor.userInfo.Id_};
        cursor=db.query(Time_contractor.userInfo.TABLE,projection,null,null,null,null,null);
        return cursor;
    }


    public void deleteRow(int id, SQLiteDatabase db) {
        //String selection = Time_contractor.userInfo.Id_ + " LIKE?";
        //String[] temp_id = {str_id};
        db.delete(Time_contractor.userInfo.TABLE,Time_contractor.userInfo.Id_+"="+id,null);
        //selection, temp_id);
    }


    /*
    public Cursor getMessage(String type, SQLiteDatabase db) {
        Cursor cursor;
        String[] projection =
                {
                        Time_contractor.userInfo.Id_,
                        Time_contractor.userInfo.Msg,
                        Time_contractor.userInfo.Num,
                        Time_contractor.userInfo.Date_,
                        //Time_contractor.userInfo.Time,
                        Time_contractor.userInfo.Status
                };

        String selection = Time_contractor.userInfo.Id_ + " LIKE?";
        String[] temp_type = {type};
        cursor = db.query(Time_contractor.userInfo.TABLE, projection, selection, temp_type, null, null, null);
        return cursor;
    }
    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
