package ru.ifmo.ctddev.koval.rssreader.rss;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ndkoval on 1/6/14.
 */
public class Channel {

    long id;
    private String title;
    private URL url;

    public Channel(long id, String title, String url) throws MalformedURLException {
        this.id = id;
        this.title = title;
        this.url = new URL(url);
    }

    public Channel(String title, URL url) {
        this.title = title;
        this.url = url;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }
}
