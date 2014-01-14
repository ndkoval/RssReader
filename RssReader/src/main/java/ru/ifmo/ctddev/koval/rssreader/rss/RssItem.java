package ru.ifmo.ctddev.koval.rssreader.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RssItem {

    private String title;
    private String link;
    private Date pubDate;
    private String description;
    private String content;

    public RssItem() {
    }

    public RssItem(String title, String link, Date pubDate, String description, String content) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected Object clone() {
        return new RssItem(title, link, pubDate, description, content);
    }
}