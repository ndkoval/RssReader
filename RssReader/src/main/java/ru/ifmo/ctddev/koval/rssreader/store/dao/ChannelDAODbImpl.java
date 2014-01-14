package ru.ifmo.ctddev.koval.rssreader.store.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.MyApp;
import ru.ifmo.ctddev.koval.rssreader.rss.Channel;
import ru.ifmo.ctddev.koval.rssreader.store.ChannelDbOpenHelper;

public class ChannelDAODbImpl implements ChannelDAO {

    private ChannelDbOpenHelper dbOpenHelper;
    private SQLiteDatabase writableChannels;
    private SQLiteDatabase readableChannels;

    public ChannelDAODbImpl() {
        dbOpenHelper = new ChannelDbOpenHelper(MyApp.getAppContext());
        writableChannels = dbOpenHelper.getWritableDatabase();
        readableChannels = dbOpenHelper.getReadableDatabase();
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = readableChannels.rawQuery("select * from " + dbOpenHelper.DATABASE_TABLE, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ID_COLUMN));
                    String title = cursor.getString(cursor.getColumnIndex(dbOpenHelper.TITLE_COLUMN));
                    String url = cursor.getString(cursor.getColumnIndex(dbOpenHelper.URL_COLUMN));
                    try {
                        channels.add(new Channel(id, title, url));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return channels;
    }

    @Override
    public long insert(Channel channel) {
        ContentValues values = new ContentValues();
        values.put(dbOpenHelper.TITLE_COLUMN, channel.getTitle());
        values.put(dbOpenHelper.URL_COLUMN, channel.getUrl().toString());
        return writableChannels.insert(dbOpenHelper.DATABASE_TABLE, null, values);
    }

    @Override
    public void remove(long id) {
        writableChannels.delete(dbOpenHelper.DATABASE_TABLE, dbOpenHelper.ID_COLUMN + "=" + id, null);
    }

    @Override
    public void update(long id, Channel channel) {
        ContentValues values = new ContentValues();
        values.put(dbOpenHelper.TITLE_COLUMN, channel.getTitle());
        values.put(dbOpenHelper.URL_COLUMN, channel.getUrl().toString());
        writableChannels.update(dbOpenHelper.DATABASE_TABLE, values, dbOpenHelper.ID_COLUMN + "=" + id, null);
    }
}
