package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "test3.db";
    //static final int DATABASE_VERSION = 2;
    /*
    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }*/

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }
    public Cursor select()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("images",new String[]{"image"},null,null,null,null,null);
        return c;
    }

    public void insert(String image_uri,String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues value = new ContentValues();
        value.put("id", id);
        Log.d("putting id=====>>>   ",String.valueOf(id));
        value.put("image", image_uri);
        db.insert("images",null,value);
        db.close();
    }
    public void delete(String id)
    {
        //id++;
        SQLiteDatabase db = getWritableDatabase();
        //db.delete("images","image",new String[]{image});
        Log.d("delete ==== >>>>>>>","delete complete");
        Log.d("delete id == >>>>>>>",String.valueOf(id));
        db.delete("images","id=?",new String[]{id});
        //db.delete("images","id",null);
        //db.execSQL("DELETE FROM images WHERE id = " + 1);
        db.close();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //String sql = "create table images ( id TEXT, image TEXT)";
        String sql = "create table images (image TEXT, id TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

}

