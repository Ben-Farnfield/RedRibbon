package uk.ac.bradford.pisoc.redribbon.service.update_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author 	Ben Farnfield
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
	 * @see	UpdateBroadcastReceiver#CLIENT_START
	 * @see	UpdateBroadcastReceiver#USER_REFRESH
	 * @see	UpdateBroadcastReceiver#ALARM_REFRESH
	 */
	public static final String EXTRA_BROADCAST_TYPE = "broadcast_type";
	
	/**
	 * Broadcast sent when a client has started.
	 */
	public static final String CLIENT_START = "client_start";
	
	/**
	 * Broadcast sent when a user requests a refresh.
	 */
	public static final String USER_REFRESH = "user_refresh";
	
	/**
	 * Broadcast sent when AlarmManager requests a refresh.
	 * 
	 * @see	AlarmManager
	 */
	public static final String ALARM_REFRESH = "alarm_refresh";
	
	/**
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

	}
}
