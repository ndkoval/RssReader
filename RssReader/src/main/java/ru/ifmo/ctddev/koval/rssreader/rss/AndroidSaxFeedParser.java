package ru.ifmo.ctddev.koval.rssreader.rss;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser {

    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }

    public List<RssItem> parse() throws IOException, SAXException {
        final RssItem currentItem = new RssItem();
        RootElement root = new RootElement("rss");
        final List<RssItem> messages = new ArrayList<>();
        Element channel = root.getChild("channel");
        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                messages.add((RssItem) currentItem.clone());
            }
        });
        item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setTitle(body);
            }
        });
        item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setLink(body);
            }
        });
        item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setDescription(body);
            }
        });
        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setPubDate(body);
            }
        });

        Xml.parse(this.getInputStream(),
                Xml.Encoding.UTF_8,
                root.getContentHandler()
        );

        return messages;
    }
}