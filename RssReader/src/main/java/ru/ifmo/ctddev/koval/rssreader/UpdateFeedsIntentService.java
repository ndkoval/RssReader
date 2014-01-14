package ru.ifmo.ctddev.koval.rssreader;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.rss.AndroidSaxFeedParser;
import ru.ifmo.ctddev.koval.rssreader.rss.Channel;
import ru.ifmo.ctddev.koval.rssreader.rss.RssItem;
import ru.ifmo.ctddev.koval.rssreader.store.ChannelList;
import ru.ifmo.ctddev.koval.rssreader.store.EntryStore;

public class UpdateFeedsIntentService extends IntentService {

    public UpdateFeedsIntentService() {
        super(UpdateFeedsIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent broadcastIntent = new Intent(Constants.RECEIVER);
        broadcastIntent.putExtra(Constants.RECEIVER_DATA, Constants.STATUS_RUNNING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        try {
            for (Channel channel : ChannelList.getInstance().getAll()) {
                List<RssItem> entries = null;

                HttpURLConnection conn = (HttpURLConnection) channel.getUrl().openConnection();
                InputStream is = conn.getInputStream();

                entries = new AndroidSaxFeedParser(channel.getUrl().toString()).parse();

                EntryStore.getInstance().updateEntries(channel.getId(), entries);
            }

        } catch (IOException | SAXException e) {
            e.printStackTrace();
            broadcastIntent = new Intent(Constants.RECEIVER);
            broadcastIntent.putExtra(Constants.RECEIVER_DATA, Constants.STATUS_ERROR);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
            return;
        }

        broadcastIntent = new Intent(Constants.RECEIVER);
        broadcastIntent.putExtra(Constants.RECEIVER_DATA, Constants.STATUS_FINISHED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
