package ru.ifmo.ctddev.koval.rssreader.store.dao;

import java.util.List;

import ru.ifmo.ctddev.koval.rssreader.rss.Channel;

/**
 * Created by ndkoval on 1/6/14.
 */
public interface ChannelDAO {

    List<Channel> getChannels();

    long insert(Channel channel);

    void remove(long id);

    void update(long id, Channel channel);
}
