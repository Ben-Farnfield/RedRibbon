package uk.ac.bradford.pisoc.redribbon.activity;

import java.util.List;

import uk.ac.bradford.pisoc.redribbon.R;
import uk.ac.bradford.pisoc.redribbon.data.db.ItemDAO;
import uk.ac.bradford.pisoc.redribbon.data.model.Item;
import uk.ac.bradford.pisoc.redribbon.service.update_service.UpdateBroadcastReceiver;
import uk.ac.bradford.pisoc.redribbon.service.update_service.UpdateService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RedRibbonMock extends Activity {
	
	private static final String TAG = "RedRibbonMock";
	
	private Button mRefreshButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redribbon_mock);
		
		mRefreshButton = (Button)findViewById(R.id.refresh_button);
		mRefreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendBroadcast(
						new Intent(UpdateBroadcastReceiver.USER_REFRESH));
			}
		});
		
		registerRefreshCompleteReceiver();
		sendClientStartedBroadcast();
	}
	
	private void registerRefreshCompleteReceiver() {
		LocalBroadcastManager.getInstance(this)
				.registerReceiver(mRefreshCompleteReceiver, 
						new IntentFilter(UpdateService.REFRESH_COMPLETE));
	}
	
	private void sendClientStartedBroadcast() {
		sendBroadcast(new Intent(UpdateBroadcastReceiver.CLIENT_STARTED));
	}
	
	@Override
	protected void onDestroy() {
		unregisterRefreshCompleteReceiver();
		super.onDestroy();
	}
	
	private void unregisterRefreshCompleteReceiver() {
		LocalBroadcastManager.getInstance(this)
				.unregisterReceiver(mRefreshCompleteReceiver);
	}
	
	private BroadcastReceiver mRefreshCompleteReceiver = 
			new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(RedRibbonMock.this, 
					"Refresh Complete", Toast.LENGTH_SHORT).show();
			
			ItemDAO dao = new ItemDAO(RedRibbonMock.this);
			dao.open();
			List<Item> items = dao.getItems();
			dao.close();
			
			for (Item item : items) {
				Log.d(TAG, item.toString());
			}
		}
	};
}
