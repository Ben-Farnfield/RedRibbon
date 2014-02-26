package uk.ac.bradford.pisoc.redribbon.data.model;

import java.util.Date;

/**
 * This class represents one item from an RSS 2.0 feed.
 * 
 * <pre>
 *     <code>
 *         &lt;item&gt;
 *             &lt;title&gt;Title&lt;/title&gt;
 *             &lt;description&gt;Body text&lt;/description&gt;
 *             &lt;pubDate&gt;Event date&lt;/pubDate&gt;
 *         &lt;/item&gt;
 *     </code>
 * </pre>
 * 
 * Item objects are used to pass item data between:
 * <ul>
 *     <li>The RssFeedParser and SQLite database.</li>
 *     <li>The SQLite database and UI.</li>
 * </ul>
 * 
 * @author 	Ben Farnfield
 * @see		RssFeedParser
 * @see		ItemDAO
 */
public class Item {
	
	private String mTitle;
	private String mBody;
	private Date mEventDate;
	private Date mUpdateCreated;
	
	/**
	 * Default constructor.
	 */
	public Item() {
		super();
	}
	
	/**
	 * @return 	String containing the title of the update.
	 */
	public String getTitle() {
		return mTitle;
	}
	/**
	 * @param 	title String representing the title of the update.
	 */
	public void setTitle(String title) {
		mTitle = title;
	}
	
	/**
	 * @return 	String containing the main body text for the update.
	 */
	public String getBody() {
		return mBody;
	}
	/**
	 * @param 	body String representing the main body text for the update.
	 */
	public void setBody(String body) {
		mBody = body;
	}
	
	/**
	 * @return 	Date object containing the date of the event related to this 
	 * 			update.
	 */
	public Date getEventDate() {
		return mEventDate;
	}
	/**
	 * @param 	eventDate Date object representing the date of the event 
	 * 			related to this update.
	 */
	public void setEventDate(Date eventDate) {
		mEventDate = eventDate;
	}
	
	/**
	 * @return 	Date object containing the date this update was sent.
	 */
	public Date getUpdateCreated() {
		return mUpdateCreated;
	}
	/**
	 * @param 	updateCreated Date object representing the date this update 
	 * 			was sent.
	 */
	public void setUpdateCreated(Date updateCreated) {
		mUpdateCreated = updateCreated;
	}
}
