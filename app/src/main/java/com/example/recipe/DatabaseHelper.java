package com.example.recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipe_database.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PROFILE_IMAGE = "profile_image";
    public static final String COLUMN_PROFILE_ID = "id";
    public static final String COLUMN_PROFILE_IMAGE_URI = "image_uri";

    public static final String TABLE_FAV_RECIPE = "fav_recipe";
    public static final String COLUMN_FAV_ID = "id";
    public static final String COLUMN_FAV_IMAGE_URI = "image_uri";
    public static final String COLUMN_FAV_TITLE = "recipe_title";
    public static final String COLUMN_FAV_STATUS = "fStatus";

    private static final String CREATE_TABLE_PROFILE_IMAGE =
            "CREATE TABLE " + TABLE_PROFILE_IMAGE + " (" +
                    COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROFILE_IMAGE_URI + " TEXT);";

    private static final String CREATE_TABLE_FAVORITE_RECIPE =
            "CREATE TABLE " + TABLE_FAV_RECIPE + " (" +
                    COLUMN_FAV_ID + " TEXT, " +
                    COLUMN_FAV_TITLE + " TEXT, " +
                    COLUMN_FAV_IMAGE_URI + " TEXT, " +
                    COLUMN_FAV_STATUS + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROFILE_IMAGE);
        db.execSQL(CREATE_TABLE_FAVORITE_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_RECIPE);
        onCreate(db);
    }

    public void insertEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        for (int x = 1; x < 5; x++){
            cv.put(COLUMN_FAV_ID, x);
            cv.put(COLUMN_FAV_STATUS, "0");

            db.insert(TABLE_FAV_RECIPE, null, cv);
        }
    }

    public void insertIntoTheDatabase(String item_title, int item_image, String id, String fav_status) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_FAV_TITLE, item_title);
            cv.put(COLUMN_FAV_IMAGE_URI, item_image);
            cv.put(COLUMN_FAV_ID, id);
            cv.put(COLUMN_FAV_STATUS, fav_status);
            db.insert(TABLE_FAV_RECIPE, null, cv);
            Log.d("DatabaseHelper Status", item_title + ", favstatus - " + fav_status + " - . " + cv);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    public Cursor read_all_data (String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "SELECT * FROM " + TABLE_FAV_RECIPE + " WHERE " + COLUMN_FAV_ID + "=" + id + "";
            cursor = db.rawQuery(sql, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Database Error", "Error reading data: " + e.getMessage());
            return null;
        }
    }

    public void remove_fav(String id){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "UPDATE " + TABLE_FAV_RECIPE + " SET " + COLUMN_FAV_STATUS + " ='0' WHERE " + COLUMN_FAV_ID + "=" + id + "";
            db.execSQL(sql);
            Log.d("remove", id.toString());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public Cursor select_all_favorite_list(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "SELECT * FROM " + TABLE_FAV_RECIPE + " WHERE " + COLUMN_FAV_STATUS + " ='1'";
            cursor = db.rawQuery(sql, null);
            return cursor;
        } catch (Exception e) {
            Log.e("Database Error", "Error selecting data: " + e.getMessage());
            return null;
        }
    }

}

