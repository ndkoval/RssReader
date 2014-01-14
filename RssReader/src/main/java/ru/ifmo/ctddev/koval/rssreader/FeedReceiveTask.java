package ru.ifmo.ctddev.koval.rssreader;

import android.database.Cursor;
import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.rss.AndroidSaxFeedParser;
import ru.ifmo.ctddev.koval.rssreader.rss.RssItem;
import ru.ifmo.ctddev.koval.rssreader.store.EntryStore;

public class FeedReceiveTask extends AsyncTask<Void, Void, Cursor> {

    private final TitleCursorAdapter titleAdapter;
    private final URL url;
    private final long feedId;

    public FeedReceiveTask(TitleCursorAdapter titleAdapter, URL url, long feedId) {
        this.titleAdapter = titleAdapter;
        this.url = url;
        this.feedId = feedId;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {

        List<RssItem> entries = null;

        try {
            entries = new AndroidSaxFeedParser(url.toString()).parse();
        } catch (IOException | SAXException e) {
            return null;
        }

        EntryStore.getInstance().updateEntries(feedId, entries);
        return EntryStore.getInstance().getItemsCursor(feedId);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

        if (cursor == null) {
            return;
        }

        titleAdapter.changeCursor(cursor);
        titleAdapter.notifyDataSetInvalidated();
    }
}
