package com.bam.darkhouseextreme.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anders on 2015-04-28.
 */
public class DatabaseHelper extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "DarkHouse.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Player";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String MAP_X = "MapXCoordinate";
    private static final String MAP_Y = "MapYCoordinate";
    private static final String OBJ_IDS = "ObjectIds";
    private static final String SCORE = "Score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + NAME + " TEXT, " + MAP_X + " INTEGER, " + MAP_Y + " INTEGER, " + OBJ_IDS + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS TABLE " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        long result = db.insert(TABLE_NAME, null, contentValues);

        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getOneCharacter(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + "WHERE " + ID + " = " + id, null);
        db.close();
        return cursor;
    }

    public Cursor getAllCharacters() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        db.close();
        return cursor;
    }

    public boolean updateCharacter(String id, String mapXCoordinate, String mapYCoordinate, int score, String[] objectIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAP_X, mapXCoordinate);
        contentValues.put(MAP_Y, mapYCoordinate);
        contentValues.put(SCORE, score);
        String whereClause = "WHERE ID = ?";
        String [] whereArgs = {id};
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs);

        for (String s : objectIds) {
            contentValues.put(OBJ_IDS, s);
            db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        }
        return true;
    }

    public boolean deleteCharacter(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "WHERE ID = ?";
        String[] whereArgs = {id};
        db.delete(TABLE_NAME, whereClause, whereArgs);

        return true;
    }

    public void addObjectToInventory(String playerId, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBJ_IDS, objectId);
        String whereClause = "WHERE ID = ?";
        String[] whereArgs = {playerId};
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
    }
    public void removeObjectFromInventory(String playerId, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.execSQL("DELETE " + OBJ_IDS + "FROM " + TABLE_NAME + "WHERE " + ID + " = " + playerId + " AND " + OBJ_IDS + " = " + objectId );

        contentValues.put(OBJ_IDS, objectId);
        String whereClause = "WHERE ID = ?";
        String[] whereArgs = {playerId};
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
    }



}
