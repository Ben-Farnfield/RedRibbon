package uk.ac.bradford.pisoc.redribbon.service.update_service;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import uk.ac.bradford.pisoc.redribbon.util.Network;
import uk.ac.bradford.pisoc.redribbon.util.RssFeedParser;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class UpdateService extends IntentService {
	
	private static final String TAG = "UpdateService";
	
	private static final String URL = 
			"http://blog.vogella.com/comments/feed/";
	
	/**
	 * 
	 */
	public static final String REFRESH_COMPLETE = "refresh_complete";
	
	/**
	 * Default constructor.
	 */
	public UpdateService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		List<Item> items = null;
		try {
			items = RssFeedParser.parse(
					Network.getConnection(this, URL).getInputStream());
		} catch (XmlPullParserException e) {
			return;
		} catch (IOException e) {
			return;
		}
		
		//TODO DEBUG
		for (Item item : items) {
			Log.d(TAG, item.toString());
		}
		
		if (true) { // only send if new update received
			sendNotification(null);
		}
		
		sendRefreshCompleteBroadcast(intent);
	}
	
	private void sendNotification(Item item) {
		
	}
	
	private void sendRefreshCompleteBroadcast(Intent intent) {
		String broadcastType = intent.getStringExtra(
				UpdateBroadcastReceiver.EXTRA_BROADCAST_TYPE);
		
		if (UpdateBroadcastReceiver.USER_REFRESH.equals(broadcastType)) {
			LocalBroadcastManager.getInstance(this)
					.sendBroadcast(new Intent(REFRESH_COMPLETE));
		}
	}
}
