package ru.ifmo.ctddev.koval.rssreader.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.MyApp;
import ru.ifmo.ctddev.koval.rssreader.rss.RssItem;

public class EntryStore {

    private static EntryStore instance = new EntryStore(MyApp.getAppContext());
    private EntryDbOpenHelper dbOpenHelper;
    private SQLiteDatabase writableItems;
    private SQLiteDatabase readableItems;

    private EntryStore(Context context) {
        dbOpenHelper = new EntryDbOpenHelper(context);
        writableItems = dbOpenHelper.getWritableDatabase();
        readableItems = dbOpenHelper.getReadableDatabase();
    }

    public static EntryStore getInstance() {
        return instance;
    }

    public void updateEntries(long feedId, List<RssItem> newItems) {
        String deleteSQL = "delete from "
                + dbOpenHelper.DATABASE_TABLE
                + " where " + dbOpenHelper.FEED_ID_COLUMN + " = " + feedId;

        try {
            writableItems.beginTransaction();
            writableItems.execSQL(deleteSQL);

            for (RssItem item : newItems) {
                ContentValues values = new ContentValues();
                values.put(dbOpenHelper.TITLE_COLUMN, item.getTitle());
                values.put(dbOpenHelper.DESCRIPTION_COLUMN, item.getDescription());
                values.put(dbOpenHelper.URL_COLUMN, item.getLink());
                values.put(dbOpenHelper.FEED_ID_COLUMN, feedId);
                writableItems.insert(dbOpenHelper.DATABASE_TABLE, null, values);
            }

            writableItems.setTransactionSuccessful();
        } finally {
            writableItems.endTransaction();
        }
    }

    public Cursor getItemsCursor(long feedId) {
        String query = "select * from "
                + dbOpenHelper.DATABASE_TABLE
                + " where " + dbOpenHelper.FEED_ID_COLUMN + " = " + feedId;
        return readableItems.rawQuery(query, null);
    }


}
