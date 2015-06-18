package com.hybridplay.center;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HP_Connect extends HP_Drawer {

	private static HP_Connect activity;
	WifiManager mainWifi;
	WifiReceiver receiverWifi;

	StringBuilder sb = new StringBuilder();

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hp_community);

		activity = this;

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		receiverWifi = new WifiReceiver();

		if(mainWifi.isWifiEnabled()==false){
			// If WIFI disabled then enable it
			String tMsg = "Wifi is disabled...enabling it";
			Toast toast = Toast.makeText(activity, (CharSequence) tMsg,Toast.LENGTH_LONG);
			toast.getView().setBackgroundColor(getResources().getColor(R.color.hp_green));
			toast.show();

			mainWifi.setWifiEnabled(true);
		}

		registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		doInback();
	}

	public void doInback(){
		handler.postDelayed(new Runnable() {

			@Override
			public void run(){
				// TODO Auto-generated method stub
				mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

				receiverWifi = new WifiReceiver();
				registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
				mainWifi.startScan();
				doInback();
			}
		}, 1000);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Refresh");
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		mainWifi.startScan();
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onPause(){
		unregisterReceiver(receiverWifi);
		super.onPause();
	}

	@Override
	protected void onResume(){
		registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	class WifiReceiver extends BroadcastReceiver{
		
		public void onReceive(Context c, Intent intent){

			ArrayList<String> connections = new ArrayList<String>();
			ArrayList<Integer> Signal_Strength= new ArrayList<Integer>();

			sb = new StringBuilder();
			List<ScanResult> wifiList;
			wifiList = mainWifi.getScanResults();
			for(int i = 0; i < wifiList.size(); i++){
				connections.add(wifiList.get(i).SSID);
				Signal_Strength.add(wifiList.get(i).level);
			}


		}
	}

}
