package com.meitu.meitutietie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DBHelper {

    private static final String DB_NAME = "usersDatabase";
    private static final String TABLE_Name = "usersDB";

    public DBHelper() {
    }


    /**
     * 添加下载的主题包id到数据库中
     *
     * @param id
     */
    public static void addDownload(int id,String thumbnailPath, String name, int count,
                                   Context context) {

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("download", true);
        values.put("turnOn", true);
        values.put("thumbnailPath", thumbnailPath);
        values.put("name",name);
        values.put("count",count);
        db.insert(TABLE_Name, null, values);
    }


    /**
     * 查询该id的主题包是否已经被下载
     *
     * @param id
     * @return
     */
    public static boolean queryDownload(int id, Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_Name, new String[]{"download"}, "id = " + id, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                boolean res = cursor.getInt(0) > 0;
                return res;
            }
        }
        cursor.close();
        db.close();
        return false;
    }


    /**
     *  查询下载的主题
     * @param context
     * @return
     */
    public static ArrayList<StickerTheme> getDataFromDatabase (Context context){
        ArrayList<StickerTheme> res = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_Name, new String[]{"thumbnailPath","name","count","turnOn","id"}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String thumbnailPath = cursor.getString(0);
                String name = cursor.getString(1);
                int count = cursor.getInt(2);
                int turnOn = cursor.getInt(3);
                int id = cursor.getInt(4);
                StickerTheme theme = new StickerTheme();
                theme.setThumbnailUrl(thumbnailPath);
                theme.setName(name);
                theme.setCount(count);
                theme.setTurnOn(turnOn>0?true:false);
                theme.setId(id);
                res.add(theme);
            }
        }
        cursor.close();
        db.close();
        return res;
    }


    /**
     * 更新主题状态（是否应用此主题）
     *
     * @param context
     * @param state
     * @param id
     */
    public static void switchUpdate (Context context, boolean state, int id) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("turnOn", state);
        db.update(TABLE_Name, values, "id=" + id,null);
    }


    /**
     * 获取被激活的主题list
     *
     * @param context
     * @return
     */
    public static Set<Integer> getValidThemes (Context context) {
        Set<Integer> s = new HashSet<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_Name, new String[]{"turnOn","id"}, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getInt(0) > 0){
                    s.add(cursor.getInt(1));
                }
            }
        }
        cursor.close();
        db.close();
        return s;
    }


    /**
     * 删除对应id的主题
     *
     * @param context
     * @param id
     */
    public static void deleteTheme(Context context, int id) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int succeed = db.delete(TABLE_Name, "id = " + id,null);
    }

}
