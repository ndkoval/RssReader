package ru.ifmo.ctddev.koval.rssreader.rss;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser {

    private static final SimpleDateFormat RSS_DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    private static final SimpleDateFormat ATOM_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    private static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }

    private List<RssItem> parseAtom() {
        final RssItem currentItem = new RssItem();
        RootElement root = new RootElement(ATOM_NAMESPACE, "feed");
        final List<RssItem> items = new ArrayList<>();
        Element item = root.getChild(ATOM_NAMESPACE, "entry");
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add((RssItem) currentItem.clone());
            }
        });
        item.getChild(ATOM_NAMESPACE, TITLE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setTitle(body);
            }
        });
        item.getChild(ATOM_NAMESPACE, LINK).setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                currentItem.setLink(attributes.getValue("href"));
            }
        });
        item.getChild(ATOM_NAMESPACE, "summary").setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setDescription(body);
            }
        });
//        item.getChild("published").setEndTextElementListener(new EndTextElementListener() {
//            public void end(String body) {
//                try {
//                    currentItem.setPubDate(ATOM_DATE_FORMAT.parse(body));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        try {
            Xml.parse(this.getInputStream(),
                    Xml.Encoding.UTF_8,
                    root.getContentHandler()
            );
        } catch (IOException | SAXException e) {
            return null;
        }

        return items;
    }

    private List<RssItem> parseAtomRss() {
        final RssItem currentItem = new RssItem();
        RootElement root = new RootElement(ATOM_NAMESPACE, "rss");
        final List<RssItem> items = new ArrayList<>();
        Element channel = root.getChild(ATOM_NAMESPACE, "channel");
        Element item = channel.getChild(ATOM_NAMESPACE, ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add((RssItem) currentItem.clone());
            }
        });
        item.getChild(ATOM_NAMESPACE, TITLE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setTitle(body);
            }
        });
        item.getChild(ATOM_NAMESPACE, LINK).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setLink(body);
            }
        });
        item.getChild(ATOM_NAMESPACE, DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentItem.setDescription(body);
            }
        });
//        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
//            public void end(String body) {
//                try {
//                    currentItem.setPubDate(RSS_DATE_FORMAT.parse(body));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        try {
            Xml.parse(this.getInputStream(),
                    Xml.Encoding.UTF_8,
                    root.getContentHandler()
            );
        } catch (IOException | SAXException e) {
            return null;
        }

        return items;
    }

    private List<RssItem> parseRss() {
        final RssItem currentItem = new RssItem();
        RootElement root = new RootElement("rss");
        final List<RssItem> items = new ArrayList<>();
        Element channel = root.getChild("channel");
        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add((RssItem) currentItem.clone());
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
//        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
//            public void end(String body) {
//                try {
//                    currentItem.setPubDate(RSS_DATE_FORMAT.parse(body));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        try {
            Xml.parse(this.getInputStream(),
                    Xml.Encoding.UTF_8,
                    root.getContentHandler()
            );
        } catch (IOException | SAXException e) {
            Log.d("RSS", e.getMessage());
            return null;
        }

        return items;
    }

    public List<RssItem> parse() throws SAXException, IOException {
        List<RssItem> items = parseRss();

        if (items == null) {
            items = parseAtom();
        }

        if (items == null) {
            items = parseAtomRss();
        }

        if (items == null) {
            throw new SAXException();
        }

        return items;
    }
}