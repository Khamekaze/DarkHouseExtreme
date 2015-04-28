package com.bam.darkhouseextreme.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anders on 2015-04-28.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DarkHouse.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PLAYER_TABLE_NAME = "Player";
    private static final String PLAYER_ID = "Id";
    private static final String PLAYER_NAME = "Name";
    private static final String PLAYER_MAP_X = "MapXCoordinate";
    private static final String PLAYER_MAP_Y = "MapYCoordinate";
    private static final String PLAYER_OBJ_IDS = "ObjectIds";
    private static final String PLAYER_SCORE = "Score";

    private static final String ITEM_TABLE_NAME = "Item";
    private static final String ITEM_ID = "Id";
    private static final String ITEM_NAME = "Name";
    private static final String ITEM_DESCRIPTION = "Description";

    private static final String PLAYER_ITEM_JUNCTION_TABLE_NAME = "Player_Item";
    private static final String PLAYER_ITEM_ID = "Id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS" + PLAYER_TABLE_NAME + " (" + PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PLAYER_NAME + " TEXT, " + PLAYER_MAP_X + " INTEGER, " + PLAYER_MAP_Y + " INTEGER, " + PLAYER_OBJ_IDS + " INTEGER)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS" + ITEM_TABLE_NAME + " (" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ITEM_NAME + " TEXT, " + ITEM_DESCRIPTION + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS" + PLAYER_ITEM_JUNCTION_TABLE_NAME + " (" + PLAYER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PLAYER_ID + " TEXT REFERENCES " + PLAYER_TABLE_NAME + ", " + ITEM_ID + " TEXT REFERENCES" + ITEM_TABLE_NAME + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS TABLE " + PLAYER_TABLE_NAME);
        onCreate(db);
    }

    public boolean createCharacter(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_NAME, name);
        long result = db.insert(PLAYER_TABLE_NAME, null, contentValues);

        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getOneCharacter(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYER_TABLE_NAME + "WHERE " + PLAYER_ID + " = " + id, null);
        db.close();
        return cursor;
    }

    public Cursor getAllCharacters() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYER_TABLE_NAME, null);
        db.close();
        return cursor;
    }

    public boolean updateCharacter(String id, String mapXCoordinate, String mapYCoordinate, int score, String[] objectIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_MAP_X, mapXCoordinate);
        contentValues.put(PLAYER_MAP_Y, mapYCoordinate);
        contentValues.put(PLAYER_SCORE, score);
        String whereClause = " WHERE " + PLAYER_ID + " = ?";
        String[] whereArgs = {id};
        db.update(PLAYER_TABLE_NAME, contentValues, whereClause, whereArgs);

        for (String s : objectIds) {
            contentValues.put(PLAYER_OBJ_IDS, s);
            db.update(PLAYER_TABLE_NAME, contentValues, whereClause, whereArgs);
        }
        return true;
    }

    public boolean deleteCharacter(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = " WHERE " + PLAYER_ID + " = ?";
        String[] whereArgs = {id};
        db.delete(PLAYER_TABLE_NAME, whereClause, whereArgs);

        return true;
    }

    public boolean addObjectToPlayerInventory(String playerId, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_OBJ_IDS, objectId);
        String whereClause = " WHERE " + PLAYER_ID + " = ?";
        String[] whereArgs = {playerId};

        int i = db.update(PLAYER_TABLE_NAME, contentValues, whereClause, whereArgs);
        if (i == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean removeObjectFromInventory(String playerId, String objectId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = " WHERE Id in" + "(SELECT Id FROM" + PLAYER_ITEM_JUNCTION_TABLE_NAME +
                "WHERE " + PLAYER_ID + " = ? AND " + ITEM_ID + " = ? LIMIT 1)";
        String[] whereArgs = {playerId, objectId};

        int i = db.delete(PLAYER_ITEM_JUNCTION_TABLE_NAME, whereClause, whereArgs);

          if (i == -1) {
            return false;
        } else {
            return true;
        }

//        Alternatively:
//        This is probably a worse way of doing the same thing as above but I'm keeping it 'til we know for sure that it works.
//        String whereClause = " WHERE " + PLAYER_ID + " = ? AND " + ITEM_ID + " = ? LIMIT 1";
//        String[] whereArgs = {playerId, objectId};
//        db.delete(PLAYER_ITEM_JUNCTION_TABLE_NAME, whereClause, whereArgs);
    }
}
