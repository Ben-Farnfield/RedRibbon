package uk.ac.bradford.pisoc.redribbon.service.update_service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Handles all incoming broadcasts for UpdateService.
 * 
 * @author 	Ben Farnfield
 * @see 	UpdateService
 */
public class UpdateBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "UpdateBroadcastReceiver";
	
	/**
	 * Key used to access the original broadcast type of an Intent sent by 
	 * UpdateBroadcastReceiver.
	 * 
	 * All Intents sent by UpdateBroadcastReceiver contain the original 
	 * broadcast type as an extra. Using this key you can retrieve the 
	 * the broadcast type as a String. Broadcast types are defined as 
	 * fields within UpdateBroadcastReceiver.
	 * 
	 * @see	UpdateBroadcastReceiver#CLIENT_STARTED
	 * @see	UpdateBroadcastReceiver#USER_REFRESH
	 * @see	UpdateBroadcastReceiver#BOOT_COMPLETED
	 */
	public static final String EXTRA_BROADCAST_TYPE = "broadcast_type";
	
	/**
	 * Broadcast sent when a client has started.
	 */
	public static final String CLIENT_STARTED = "client_started";
	
	/**
	 * Broadcast sent when a user has requested their updates be refreshed.
	 */
	public static final String USER_REFRESH = "user_refresh";
	
	/**
	 * Broadcast sent when the system boot has completed.
	 * 
	 * @see	AlarmManager
	 */
	public static final String BOOT_COMPLETED = "boot_completed";
	
	/**
	 * Default constructor.
	 */
	public UpdateBroadcastReceiver() {
		super();
	}
	
	/**
	 * Receives all incoming broadcasts for UpdateService.
	 * 
	 * Broadcasts:
	 * <table><tr>
	 * <td>CLIENT_STARTED</td>
	 * <td>If no repeating alarm is currently set then a new alarm 
	 *     is started.</td>
	 * </tr><tr>
	 * <td>USER_REFRESH</td>
	 * <td>Run UpdateService once. Upon completion a REFRESH_COMPLETE 
	 *     broadcast is sent.</td>
	 * </tr><tr>
	 * <td>BOOT_COMPLETED</td>
	 * <td>If no repeating alarm is currently set then a new alarm 
	 *     is started.</td>
	 * </tr></table>
	 * 
	 * @see	UpdateService
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		
		if (USER_REFRESH.equals(action)) {
			Intent userRefreshIntent = 
					new Intent(context, UpdateService.class);
			userRefreshIntent.putExtra(EXTRA_BROADCAST_TYPE, USER_REFRESH);
			context.startService(userRefreshIntent);
			
		} else if (CLIENT_STARTED.equals(action)) {
			startRefreshAlarm(context, CLIENT_STARTED);
			
		} else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			startRefreshAlarm(context, BOOT_COMPLETED);

		} else {
			Log.wtf(TAG, "You've registered an action without a receiver!");
		}
	}
	
	/*
	 * Starts a new alarm if no alarm is currently set. The broadcast type
	 * is passed with the Intent.
	 */
	private void startRefreshAlarm(Context context, String broadcastType) {
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {}
		
		if (noRefreshAlarmSet(context)) {
			Intent i = new Intent(context, UpdateService.class);
			i.putExtra(EXTRA_BROADCAST_TYPE, broadcastType);
			PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
			AlarmManager am = (AlarmManager) 
					context.getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, 
					Calendar.getInstance().getTimeInMillis(), 20*1000, pi);
		}
	}
	
	/*
	 * Checks to see if an alarm is currently set.
	 */
	private boolean noRefreshAlarmSet(Context context) {
		Intent testIntent = new Intent(context, UpdateService.class);
		return (PendingIntent.getService(
				context, 0, testIntent, PendingIntent.FLAG_NO_CREATE) == null);
	}
}
