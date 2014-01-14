package ru.ifmo.ctddev.koval.rssreader.store.dao;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import ru.ifmo.ctddev.koval.rssreader.rss.Channel;

/**
 * Created by ndkoval on 1/6/14.
 */
public class ChannelDAOMapImpl implements ChannelDAO {

    private static Map<Long, Channel> channels = new TreeMap<>();
    private static AtomicLong currentId = new AtomicLong(1500);

    static {
        try {
            channels.put(100L, new Channel(100, "ХабраХабр", "http://habrahabr.ru/rss"));
            channels.put(123L, new Channel(123, "Lenta.ru", "http://lenta.ru/rss"));
            channels.put(1456L, new Channel(1456, "Bash.im", "http://bash.im/rss"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Channel> getChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public synchronized long insert(Channel channel) {
        channel.setId(currentId.incrementAndGet());
        channels.put(currentId.get(), channel);
        return currentId.get() - 1;
    }

    @Override
    public synchronized void remove(long id) {
        channels.remove(id);
    }

    @Override
    public synchronized void update(long id, Channel channel) {
        channel.setId(id);
        channels.put(id, channel);

    }
}
