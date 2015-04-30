package com.bam.darkhouseextreme.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import com.bam.darkhouseextreme.app.model.Player;

import java.util.List;

/**
 * Created by Anders on 2015-04-28.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String LOG_DATA = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "DarkHouse.db";
    private static final int DATABASE_VERSION = 2;
    private static final String PLAYER_TABLE_NAME = "Player";
    private static final String PLAYER_ID = "Id";
    private static final String PLAYER_NAME = "Name";
    private static final String PLAYER_MAP_X = "MapXCoordinate";
    private static final String PLAYER_MAP_Y = "MapYCoordinate";
    private static final String PLAYER_SCORE = "Score";

    private static final String ITEM_TABLE_NAME = "Item";
    private static final String ITEM_ID = "Id";
    private static final String ITEM_NAME = "Name";
    private static final String ITEM_DESCRIPTION = "Description";

    private static final String PLAYER_ITEM_JUNCTION_TABLE_NAME = "Player_Item";
    private static final String PLAYER_ITEM_ID = "Id";
    private static final String JUNCTION_TABLE_PLAYER_ID = "Player_Id";
    private static final String JUNCTION_TABLE_ITEM_ID = "Item_Id";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLAYER_TABLE_NAME + " (" + PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PLAYER_NAME + " TEXT, " + PLAYER_MAP_X + " INTEGER, " + PLAYER_MAP_Y + " INTEGER)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ITEM_TABLE_NAME + " (" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ITEM_NAME + " TEXT, " + ITEM_DESCRIPTION + " TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLAYER_ITEM_JUNCTION_TABLE_NAME + " (" + PLAYER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + JUNCTION_TABLE_PLAYER_ID + " INTEGER REFERENCES " + PLAYER_TABLE_NAME + ", " + JUNCTION_TABLE_ITEM_ID + " INTEGER REFERENCES " + ITEM_TABLE_NAME + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS TABLE " + PLAYER_TABLE_NAME);
        onCreate(db);
    }

    public Player createCharacter(String name) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_NAME, name);
        long rowId = db.insert(PLAYER_TABLE_NAME, null, contentValues);

        db.close();

        if (rowId == -1) {
            Log.v(LOG_DATA, "Failed");
            return null;
        } else {
            Log.v(LOG_DATA, "Success");
            return new Player(rowId, name);
        }
    }

    public Cursor getOneCharacter(String id) {
        db = this.getReadableDatabase();
        String[] selection = {id};
        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYER_TABLE_NAME + " WHERE " + PLAYER_ID + " = ?", selection);
        db.close();
        return cursor;
    }

    public Cursor getAllCharacters() {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYER_TABLE_NAME, null);
        db.close();
        return cursor;
    }

    public boolean updateCharacter(String id, String mapXCoordinate, String mapYCoordinate, int score) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_MAP_X, mapXCoordinate);
        contentValues.put(PLAYER_MAP_Y, mapYCoordinate);
        contentValues.put(PLAYER_SCORE, score);
        String whereClause = PLAYER_ID + " = ?";
        String[] whereArgs = {id};
        db.update(PLAYER_TABLE_NAME, contentValues, whereClause, whereArgs);

        return true;
    }

    public boolean deleteCharacter(String id) {

        db = this.getWritableDatabase();
        String whereClause = PLAYER_ID + " = ?";
        String[] whereArgs = {id};
        db.delete(PLAYER_ITEM_JUNCTION_TABLE_NAME, whereClause, whereArgs);
        db.delete(PLAYER_TABLE_NAME, whereClause, whereArgs);

        return true;
    }

    public boolean addObjectToPlayerInventory(String playerId, String itemId) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JUNCTION_TABLE_PLAYER_ID, playerId);
        contentValues.put(JUNCTION_TABLE_ITEM_ID, itemId);

        long rowId = db.insert(PLAYER_ITEM_JUNCTION_TABLE_NAME, null, contentValues);

        if (rowId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllObjectsFromCharacter(String id) {
        db = this.getReadableDatabase();
        String[] selection = {id};
        Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYER_ITEM_JUNCTION_TABLE_NAME + " WHERE " + JUNCTION_TABLE_PLAYER_ID + " = ?", selection);
        db.close();
        return cursor;
    }

    public List<Object> getListOfPlayers(Cursor cursor) {


    }



    public boolean removeObjectFromInventory(String playerId, String itemId) {
        db = this.getWritableDatabase();

        String whereClause = " Id in " + "(SELECT Id FROM " + PLAYER_ITEM_JUNCTION_TABLE_NAME +
                " WHERE " + JUNCTION_TABLE_PLAYER_ID + " = ? AND " + JUNCTION_TABLE_ITEM_ID + " = ? LIMIT 1)";
        String[] whereArgs = {playerId, itemId};

        int i = db.delete(PLAYER_ITEM_JUNCTION_TABLE_NAME, whereClause, whereArgs);

        if (i == -1) {
            return false;
        } else {
            return true;
        }

//        Alternatively:
//        This is probably a worse way of doing the same thing as above but I'm keeping it 'til we know for sure that it works.
//        String whereClause = " WHERE " + PLAYER_ID + " = ? AND " + ITEM_ID + " = ? LIMIT 1";
//        String[] whereArgs = {playerId, itemId};
//        db.delete(PLAYER_ITEM_JUNCTION_TABLE_NAME, whereClause, whereArgs);
    }
}
