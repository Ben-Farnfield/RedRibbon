package uk.ac.bradford.pisoc.redribbon.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import uk.ac.bradford.pisoc.redribbon.data.model.Item;

/**
 * Handles all parsing of RSS 2.0 feeds.
 * 
 * @author 	Ben Farnfield
 */
public class RssFeedParser {
	
	//private static final String TAG = "RssParser";

	private static final String CHANNEL     = "channel";
	private static final String ITEM        = "item";
	private static final String TITLE       = "title";
	private static final String DESCRIPTION = "description";
	private static final String PUB_DATE    = "pubDate";
	
	private static final SimpleDateFormat RFC8822 = 
			new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.UK);
	
	/**
	 * Default constructor.
	 */
	public RssFeedParser() {
		super();
	}
	
	/**
	 * Parses the provided RSS 2.0 InputStream into a collection of Item 
	 * objects.
	 * 
	 * @param 	inputStream	InputStream pointing to an RSS 2.0 XML file.
	 * @return	List&lt;Item&gt; with all items contained within the feed.
	 * @throws 	XmlPullParserException
	 * @throws 	IOException
	 * @see		Item
	 */
	public static List<Item> parse(InputStream inputStream) 
			throws XmlPullParserException, IOException {

		try {
			XmlPullParser pp = XmlPullParserFactory
					.newInstance()
					.newPullParser();
			pp.setInput(inputStream, "UTF-8");
			
			int eventType = pp.getEventType();
			while (true) {
				if (eventType == XmlPullParser.START_TAG) {
					if (CHANNEL.equals(pp.getName())) return parseChannel(pp);
				}
				eventType = pp.next();
			}
		} finally {
			inputStream.close();
		}
	}
	
	/* 
	 * Parses channel blocks. This may be extended in future to collect
	 * channel header data as well as items.
	 */
	private static List<Item> parseChannel(XmlPullParser pp) 
			throws XmlPullParserException, IOException {

		List<Item> items = new ArrayList<Item>();
		int eventType = pp.next();
		
		while (true) {
			if (eventType == XmlPullParser.START_TAG) {
				if (ITEM.equals(pp.getName())) items.add(parseItem(pp));
			} else if (eventType == XmlPullParser.END_TAG) {
				if (CHANNEL.equals(pp.getName())) return items;
			}
			eventType = pp.next();
		}
	}
	
	/*
	 * Parses item blocks.
	 */
	private static Item parseItem(XmlPullParser pp) 
			throws XmlPullParserException, IOException {

		Item item = new Item();
		int eventType = pp.next();
		
		while (true) {
			if (eventType == XmlPullParser.START_TAG) {
				if (TITLE.equals(pp.getName())) {
					item.setTitle(pp.nextText());
				} else if (DESCRIPTION.equals(pp.getName())) {
					item.setBody(pp.nextText());
				} else if (PUB_DATE.equals(pp.getName())) {
					try {
						item.setEventDate(RFC8822.parse(pp.nextText()));
					} catch (ParseException e) {
						item.setEventDate(null);
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (ITEM.equals(pp.getName())) return item;
			}
			eventType = pp.next();
		}
	}
}