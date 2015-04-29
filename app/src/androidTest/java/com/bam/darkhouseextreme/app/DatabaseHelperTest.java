package com.bam.darkhouseextreme.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;

import com.bam.darkhouseextreme.app.activities.StartScreenActivity;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Player;

/**
 * Created by Anders on 2015-04-29.
 */
public class DatabaseHelperTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {

    public Context context;
//    public RenamingDelegatingContext context = new RenamingDelegatingContext(getInstrumentation().getContext(),"test");
    public DatabaseHelper databaseHelper;
    public SQLiteDatabase db;

    public DatabaseHelperTest() {
        super(StartScreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(db);
    }

    public void testCreateCharacter() {
        String name = "Mugabe";
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        long rowId = db.insert("Player", null, contentValues);

        assertEquals(rowId !=-1, databaseHelper.createCharacter(name).getId() != -1);
        db.close();
    }



    @Override
    protected void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
