package ru.ifmo.ctddev.koval.rssreader.store;

import java.util.Collections;
import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.rss.Channel;
import ru.ifmo.ctddev.koval.rssreader.store.dao.ChannelDAO;
import ru.ifmo.ctddev.koval.rssreader.store.dao.ChannelDAODbImpl;

public class ChannelList {
    private static ChannelList ourInstance = new ChannelList();
    private List<Channel> channels;
    private ChannelDAO channelDAO = new ChannelDAODbImpl();

    private ChannelList() {
        upgrade();
    }

    public static ChannelList getInstance() {
        return ourInstance;
    }

    public synchronized void add(Channel channel) {
        channelDAO.insert(channel);
        channels.add(channel);
    }

    public synchronized void update(long id, Channel channel) {
        channel.setId(id);
        channelDAO.update(id, channel);
        upgrade();
    }

    public synchronized void upgrade() {
        channels = channelDAO.getChannels();
    }

    public Channel get(int position) {
        return channels.get(position);
    }

    public int size() {
        return channels.size();
    }

    public boolean isEmpty() {
        return channels.isEmpty();
    }

    public synchronized Channel remove(int position) {
        Channel res = channels.remove(position);
        channelDAO.remove(res.getId());
        return res;
    }

    public List<Channel> getAll() {
        return Collections.unmodifiableList(channels);
    }

}
