package com.example.dictionaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TranslateDatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DICTIONARY";

    public TranslateDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DICTIONARY (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "SOURCE TEXT, "
                + "TRANSLATE TEXT, "
                + "FAVORITE NUMERIC"
                + ");");
    }

    public void insertTranslate(SQLiteDatabase db, String source, String translate, int favorite) {

        ContentValues translateValues = new ContentValues();
        translateValues.put("SOURCE", source);
        translateValues.put("TRANSLATE", translate);
        db.insert("DICTIONARY", null, translateValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
