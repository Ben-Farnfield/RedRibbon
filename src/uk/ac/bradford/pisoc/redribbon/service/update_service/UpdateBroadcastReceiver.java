package uk.ac.bradford.pisoc.redribbon.service.update_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Handles all incoming broadcasts for UpdateService:
 * 
 * <ul>
 *     <li>BOOT_COMPLETED</li>
 *     <li>CLIENT_STARTED</li>
 *     <li>USER_REFRESH</li>
 *     <li>ALARM_REFRESH</li>
 * </ul>
 * 
 * @author 	Ben Farnfield
 * @see 	UpdateService
 */
public class UpdateBroadcastReceiver extends BroadcastReceiver {

	//private static final String TAG = "UpdateBroadcastReceiver";
	
	/**
	 * Key used to access the original broadcast type of an Intent sent by 
	 * UpdateBroadcastReceiver.
	 * 
	 * All Intents sent by UpdateBroadcastReceiver store the original 
	 * broadcast type as an extra. The String extra is retrievable using 
	 * this key. Broadcast types are defined as fields within 
	 * UpdateBroadcastReceiver.
	 * 
	 * @see	UpdateBroadcastReceiver#CLIENT_STARTED
	 * @see	UpdateBroadcastReceiver#USER_REFRESH
	 * @see	UpdateBroadcastReceiver#ALARM_REFRESH
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
	 * Broadcast sent when AlarmManager has requested all updates be refreshed.
	 * 
	 * @see	AlarmManager
	 */
	public static final String ALARM_REFRESH = "alarm_refresh";
	
	/**
	 * Default constructor.
	 */
	public UpdateBroadcastReceiver() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

	}
}
