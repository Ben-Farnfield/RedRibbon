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
 * 
 * @author 	Ben Farnfield
 */
public class RssFeedParser {
	
	private static final String TAG = "RssParser";

	private static final String CHANNEL     = "channel";
	private static final String ITEM        = "item";
	private static final String TITLE       = "title";
	private static final String DESCRIPTION = "description";
	private static final String PUB_DATE    = "pubDate";
	
	private SimpleDateFormat mRFC8822 = 
			new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.UK);
	
	public RssFeedParser() {
		super();
	}
	
	public List<Item> parse(InputStream inputStream) 
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
	
	private List<Item> parseChannel(XmlPullParser pp) 
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
	
	private Item parseItem(XmlPullParser pp) 
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
						item.setEventDate(mRFC8822.parse(pp.nextText()));
					} catch (ParseException e) {
						item.setEventDate(null);
					}
				} else if (ITEM.equals(pp.getName())) {
					throw new XmlPullParserException(TAG 
							+ " : found <item> in place of </item>.");
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (ITEM.equals(pp.getName())) {
					return item;
				} else if (CHANNEL.equals(pp.getName())) {
					throw new XmlPullParserException(TAG 
							+ " : found </channel> in place of </item>.");
				}
			}
			eventType = pp.next();
		}
	}
}