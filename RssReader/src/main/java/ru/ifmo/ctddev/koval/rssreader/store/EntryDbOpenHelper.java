package ru.ifmo.ctddev.koval.rssreader.store;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EntryDbOpenHelper extends SQLiteOpenHelper {

    public static final String ID_COLUMN = "_id";
    public static final String TITLE_COLUMN = "title";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String FEED_ID_COLUMN = "feed_id";
    public static final String URL_COLUMN = "url";
    //
    public static final String DATABASE_TABLE = "entries";
    private static final int DATABASE_VERSION = 1;
    //
    private static final String DATABASE_NAME = "rss_reader_entries.db";
    //
    private static final String DATABASE_CREATE_TABLE = "create table " + DATABASE_TABLE
            + " ( " + ID_COLUMN + " integer primary key autoincrement, "
            + TITLE_COLUMN + " TEXT, "
            + DESCRIPTION_COLUMN + " TEXT, "
            + FEED_ID_COLUMN + " INT, "
            + URL_COLUMN + " TEXT" + ")";

    public EntryDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("RSS_R", "create");
        sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
}
