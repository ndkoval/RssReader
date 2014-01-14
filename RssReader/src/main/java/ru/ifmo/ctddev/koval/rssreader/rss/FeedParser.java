package ru.ifmo.ctddev.koval.rssreader.rss;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public interface FeedParser {
    List<RssItem> parse() throws IOException, SAXException;
}