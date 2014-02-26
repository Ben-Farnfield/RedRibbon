package uk.ac.bradford.pisoc.redribbon.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;

public class Network {
	
	private static final String TAG = "FeedConnection";
	
	public HttpURLConnection getConnection(Context context) 
			throws IOException {

		if (isNetworkAvailable(context)) {
			URL url = new URL("http://blog.vogella.com/comments/feed/");
			return (HttpURLConnection) url.openConnection();
		} else {
			throw new IOException("Network not available");
		}
	}

	@SuppressWarnings("deprecation")
	private boolean isNetworkAvailable(Context context) {
		
	    ConnectivityManager cm = (ConnectivityManager) 
	    		context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    return cm.getBackgroundDataSetting() // devices < 4.0
	    		&& cm.getActiveNetworkInfo() != null;
	}
}