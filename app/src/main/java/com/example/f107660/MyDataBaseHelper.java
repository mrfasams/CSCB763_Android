package com.example.f107660;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    //константи за конструктора на класа
    //дава името на файла с база данни
    private static final String DATABASE_NAME = "note_database.db";
    //отговаря на номера на базата данни
    private static final int DATABASE_VERSION = 1;

    // конструктор на класа
    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // име на таблицата
    public final static String TABLE_NAME = "note";
    // индентификатор на бележката
    public final static String UID = "_ID";
    // име на потребителя
    public final static String COLUMN_TITLE_NAME = "title";

    // име на потребителя
    public final static String COLUMN_NOTE_CONTENT = "note_content";

    //създаване на базата от данни
    @Override
    public void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY  AUTOINCREMENT, " + COLUMN_TITLE_NAME + " TEXT NOT  NULL, " + COLUMN_NOTE_CONTENT+" TEXT NOT  NULL);";
        // изпълнение на SQL оператора


        database.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    // Този метод се извиква, когато базата данни трябва да бъде обновена
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // изтриваме предходната таблица при обновяването
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    // създаваме нов екземпляр от таблицата
        onCreate(db);
    }
}
