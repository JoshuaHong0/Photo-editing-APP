package com.meitu.meitutietie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    public static final String CREATE_usersDB = "create table usersDB(id INTEGER PRIMARY KEY, download BOOLEAN, turnOn BOOLEAN, thumbnailPath STRING, name STRING, count INTEGER)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_usersDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usersDB");
    }
}
