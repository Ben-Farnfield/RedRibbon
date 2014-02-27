package uk.ac.bradford.pisoc.redribbon.service.update_service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import uk.ac.bradford.pisoc.redribbon.data.db.ItemDAO;
import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import uk.ac.bradford.pisoc.redribbon.util.Network;
import uk.ac.bradford.pisoc.redribbon.util.RssFeedParser;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends IntentService {
	
	private static final String TAG = "UpdateService";
	
	private static final String URL = 
			"http://blog.vogella.com/comments/feed/";
	
	/* Used to make Toasts */
	private Handler mHandler;
	
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
	
	/**
	 * 
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();
	}

	/**
	 * 
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		String broadcastType = intent.getStringExtra(
				UpdateBroadcastReceiver.EXTRA_BROADCAST_TYPE);
		
		List<Item> items = null;
		try {
			HttpURLConnection con = Network.getConnection(this, URL);
			items = RssFeedParser.parse(con.getInputStream());
		} catch (XmlPullParserException e) {
			Log.e(TAG, e.getMessage(), e);
			return;
		} catch (IOException e) {
			makeNetworkToast(broadcastType);
			return;
		}
		
		ItemDAO dao = new ItemDAO(this);
		dao.open();
		dao.insertItems(items);
		dao.close();
		
		if (true) { // TODO send notification if new updates received
			sendNotification(null);
		}
		
		sendRefreshCompleteBroadcast(intent, broadcastType);
	}
	
	/*
	 * 
	 */
	private void sendNotification(Item item) {
		
	}
	
	/*
	 * 
	 */
	private void sendRefreshCompleteBroadcast(Intent intent, 
			String broadcastType) {
			
		if (UpdateBroadcastReceiver.USER_REFRESH.equals(broadcastType)) {
			LocalBroadcastManager.getInstance(this)
					.sendBroadcast(new Intent(REFRESH_COMPLETE));
		}
	}
	
	/*
	 * 
	 */
	private void makeNetworkToast(String broadcastType) {
		if (UpdateBroadcastReceiver.USER_REFRESH.equals(broadcastType)) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(UpdateService.this, 
							"Are you connected to the internet?", 
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
