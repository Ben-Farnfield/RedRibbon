package uk.ac.bradford.pisoc.redribbon.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Handles the creation of HTTP connections.
 * 
 * @author 	Ben Farnfield
 */
public class Network {
	
	//private static final String TAG = "FeedConnection";
	
	/**
	 * Returns a HTTP connection to the provided RSS feed.
	 * 
	 * @param 	context	Context of the caller.
	 * @param 	feedUrl	String representing the URL of the RSS feed.
	 * @return	HttpURLConnection connected to the provided RSS feed.
	 * @throws 	IOException thrown if network access is not available.
	 * @see		HttpURLConnection
	 */
	public HttpURLConnection getConnection(Context context, String feedUrl) 
			throws IOException {

		if (isNetworkAvailable(context)) {
			URL url = new URL(feedUrl);
			return (HttpURLConnection) url.openConnection();
		} else {
			throw new IOException("Network not available");
		}
	}

	@SuppressWarnings("deprecation")
	private boolean isNetworkAvailable(Context context) {
		
	    ConnectivityManager cm = (ConnectivityManager) 
	    		context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    return cm.getBackgroundDataSetting() // devices < Android 4.0
	    		&& cm.getActiveNetworkInfo() != null;
	}
}