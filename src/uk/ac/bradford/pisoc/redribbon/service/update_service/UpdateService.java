package uk.ac.bradford.pisoc.redribbon.service.update_service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import uk.ac.bradford.pisoc.redribbon.R;
import uk.ac.bradford.pisoc.redribbon.activity.RedRibbonMock;
import uk.ac.bradford.pisoc.redribbon.data.db.ItemDAO;
import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import uk.ac.bradford.pisoc.redribbon.util.Network;
import uk.ac.bradford.pisoc.redribbon.util.RssFeedParser;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
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
		int numNewItems = dao.insertItems(items);
		dao.close();
		
		if (! UpdateBroadcastReceiver.USER_REFRESH.equals(broadcastType)) {
			if (numNewItems == 1) sendNewItemNotification();
			else if (numNewItems > 1) sendNewItemsNotification(numNewItems);
		}
		
		sendRefreshCompleteBroadcast(intent, broadcastType);
	}
	
	/*
	 * 
	 */
	private void sendNewItemNotification() {
		ItemDAO dao = new ItemDAO(this);
		dao.open();
		Item item = dao.getLatestItem();
		dao.close();
		
		PendingIntent pi = PendingIntent.getActivity(
				this, 0, new Intent(this, RedRibbonMock.class), 0);
		
		Notification notification = new NotificationCompat.Builder(this)
				.setTicker(item.getTitle())
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(item.getTitle())
				.setContentText(item.getBody())
				.setContentIntent(pi)
				.setAutoCancel(true)
				.build();
		
		NotificationManager nm = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		
		nm.notify(0, notification);
	}
	
	/*
	 * 
	 */
	private void sendNewItemsNotification(int numNewItems) {
		PendingIntent pi = PendingIntent.getActivity(
				this, 0, new Intent(this, RedRibbonMock.class), 0);
		
		Notification notification = new NotificationCompat.Builder(this)
				.setTicker(numNewItems + " new updates received!")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(numNewItems + " new updates received!")
				.setContentText("")
				.setContentIntent(pi)
				.setAutoCancel(true)
				.build();
		
		NotificationManager nm = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		
		nm.notify(0, notification);
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
							"No network connection.", 
								Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
